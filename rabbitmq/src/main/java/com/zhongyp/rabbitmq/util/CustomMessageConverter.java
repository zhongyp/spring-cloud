package com.zhongyp.rabbitmq.util;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/4/2
 * @since jdk1.8
 */
public class CustomMessageConverter implements MessageConverter {

    private final String DEFAULT_CHARSET = "UTF-8";

    private Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    private SimpleMessageConverter simpleMessageConverter;

    public CustomMessageConverter(Jackson2JsonMessageConverter jackson2JsonMessageConverter, SimpleMessageConverter simpleMessageConverter) {
        this.jackson2JsonMessageConverter = jackson2JsonMessageConverter;
        this.simpleMessageConverter = simpleMessageConverter;
    }

    @Override

    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {

        Message message = null;
        if (messageProperties != null) {
            String contentType = messageProperties.getContentType();
            if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_JSON)) {
                message = jackson2JsonMessageConverter.toMessage(object, messageProperties);
            } else if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_BYTES) || contentType.contains(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)) {
                message = simpleMessageConverter.toMessage(object, messageProperties);
            } else {
                throw new UnsupportedOperationException("不支持的数据类型：" + contentType);
            }
        }
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {

        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_JSON)) {
                content = jackson2JsonMessageConverter.fromMessage(message);
            } else if (contentType != null && contentType.contains(MessageProperties.CONTENT_TYPE_BYTES) || contentType.contains(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)) {
                content = simpleMessageConverter.fromMessage(message);
            } else {
                throw new UnsupportedOperationException("不支持的数据类型：" + contentType);
            }

        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }
}
