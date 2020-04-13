package com.zhongyp.rabbitmq.util;

import lombok.NonNull;

import java.util.concurrent.*;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/19
 * @since jdk1.8
 */
public class ThreadProcessor {

    public static Future submit(@NonNull Callable task) {
        System.out.println("当前活跃线程数：" + THREAD_POOL.getActiveCount());
        return THREAD_POOL.submit(task);
    }

    /**
     * 用于处理IO阻塞时间较长的批量任务线程池
     */
    public static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(2,
            32,
            600,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1),
            Executors.defaultThreadFactory(),
            new TaskBlockingRejectedHandler()
    );


}