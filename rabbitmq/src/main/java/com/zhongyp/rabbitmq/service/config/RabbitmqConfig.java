package com.zhongyp.rabbitmq.service.config;

import com.zhongyp.rabbitmq.Bean.TranserDTO;
import com.zhongyp.rabbitmq.util.CustomMessageConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.BatchingStrategy;
import org.springframework.amqp.rabbit.core.support.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.ErrorHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Configuration
@Slf4j
public class RabbitmqConfig {

    private String host;
    private int port;
    private String username;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final String QUEUE_NAME = "spring-cloud-rabbit-queue";

    public static final String FANOUT_EXCHANGE_NAME = "spring-cloud-rabbit-fanout-exchange";

    @Bean
    public RabbitTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter();
    }

    @Bean
    public CustomMessageConverter customMessageConverter(Jackson2JsonMessageConverter jsonMessageConverter, SimpleMessageConverter simpleMessageConverter) {
        return new CustomMessageConverter(jsonMessageConverter, simpleMessageConverter);
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, CustomMessageConverter customMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setReceiveTimeout(6L);
        factory.setMaxConcurrentConsumers(8);
        factory.setConcurrentConsumers(2);
        factory.setIdleEventInterval(6L);
        factory.setPrefetchCount(50);
        factory.setConsumerTagStrategy(consumerTagStrategy());
        factory.setMessageConverter(customMessageConverter);
        return factory;
    }

//    @Bean
//    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//
//            }
//        });
//        simpleMessageListenerContainer.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message message) {
//
//            }
//        });
//        simpleMessageListenerContainer.setMessageListener();
//    }
//
//    @Bean
//    public MessageListenerAdapter messageListenerAdapter() {
//    }

    @Bean
    public ConsumerTagStrategy consumerTagStrategy() {
        return new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return new StringBuilder("fpay-pay:consumer-").append(s).toString();
            }
        };
    }

    @Bean(value = "rabbitAdmin")
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin;
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue(QUEUE_NAME, true, false, false, null);
        queue.setAdminsThatShouldDeclare(rabbitAdmin());
        return queue;
    }

    @Bean
    public Exchange fanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(FANOUT_EXCHANGE_NAME, true, false);
        fanoutExchange.setAdminsThatShouldDeclare(rabbitAdmin());
        return fanoutExchange;
    }

    @Bean
    public Exchange topicExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange("topic", true, false);
        fanoutExchange.setAdminsThatShouldDeclare(rabbitAdmin());
        return fanoutExchange;
    }

    @Bean
    public Binding binding(Queue queue, Exchange fanoutExchange) {
        return BindingBuilder.bind(queue).to((FanoutExchange) fanoutExchange);
    }

    @Bean
    public ConnectionNameStrategy connectionNameStrategy() {
        return new SimplePropertyValueConnectionNameStrategy("支付服务");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setConnectionNameStrategy(connectionNameStrategy());
        connectionFactory.setChannelCacheSize(60);
        // 可以是设置当前实例的名称或者其他属性，在控制台的connenction中可以查看
        connectionFactory.getRabbitConnectionFactory().getClientProperties().put("custom-client-key", "custom-client-value");
        // 设置发送消息异常，会添加默认的监听，输出错误日志
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * 普通发送消息rabbitTemplate类
     * 使用单独的发送链接
     * 重试和退避策略
     * 消息发送至交换器找不到队列，记录日志
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter, RabbitTemplate.ReturnCallback returnCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        /**
         * 一般情况下可以使用setUsePublisherConnection参数为生产者新建连接
         * 但是，如果使用独占队列的话，必须将setUsePublisherConnection参数值设为false
         * 因为RabbitAdmin为Listener容器声明独占队列，如果设置为true，则会导致容器和独占队列的连接使用的是不同的连接
         * 导致容器不能使用队列
         */
        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.containerAckMode(AcknowledgeMode.AUTO);
        RetryTemplate retryTemplate = new RetryTemplate();
        // 重试策略，设置最大重试次数，和异常类型
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, Collections.<Class<? extends Throwable>, Boolean>singletonMap(Exception.class, true));
        // 幂等恢复策略
        // BackOffPolicy的实现，可以使用指数函数增加给定集中每次重试尝试的退避时间。此实现是线程安全的，适用于并发访问。对配置的修改不会影响任何正在进行的重试集。
        // setInitialInterval（long）属性控制传递给Math.exp（double）的初始值，而setMultiplier（double）属性控制此值在每次后续尝试中增加多少
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        // 初始化时间间隔,不是上来就开始
        backOffPolicy.setInitialInterval(5000L);
        // 乘数
        backOffPolicy.setMultiplier(2.0D);
        // 最大时间间隔
        backOffPolicy.setMaxInterval(100000L);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }

    /**
     * 事务RabbitTemplate不需要重试，失败就直接回滚
     */
    @Bean
    public RabbitTemplate transactionalRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter, RabbitTemplate.ReturnCallback returnCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }


    /**
     * 事务RabbitTemplate不需要重试，失败就直接回滚
     */
    @Bean
    public BatchingRabbitTemplate batchTransactionalRabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter, RabbitTemplate.ReturnCallback returnCallback) {
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(2, 100, 60 * 1000);
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            @SneakyThrows
            public void handleError(Throwable throwable) {
                System.out.println(throwable.getMessage());
                throw throwable;
            }
        };
        ((ConcurrentTaskScheduler) taskScheduler).setErrorHandler(errorHandler);
        BatchingRabbitTemplate rabbitTemplate = new BatchingRabbitTemplate(batchingStrategy, taskScheduler);
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(returnCallback);
        return rabbitTemplate;
    }

    /**
     * Json转换
     *
     * @param classMapper
     * @return
     */

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> classMapping = new HashMap<>();
        classMapping.put("string", String.class);
        classMapping.put("transerDTO", TranserDTO.class);
        classMapper.setIdClassMapping(classMapping);
        return classMapper;
    }

    /**
     * 消息发送失败的一个监听
     *
     * @return
     */
    @Bean
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                log.error("支付系统发送消息未找到交换器信息，报错信息如下：replyText-{}, exchange-{}, routingKey-{}, body-{}", s, s1, s2, new String(message.getBody()));
            }
        };
    }
}
