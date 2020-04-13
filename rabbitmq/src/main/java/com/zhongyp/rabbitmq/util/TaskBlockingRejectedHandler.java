package com.zhongyp.rabbitmq.util;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/19
 * @since jdk1.8
 */
public class TaskBlockingRejectedHandler implements RejectedExecutionHandler {

    /**
     * <p>任务出发拒绝策略后，判断当前线程池是否已满</p>
     * 是，则等待100ms
     * 否，则提交任务到线程池
     *
     * @param runnable
     * @param threadPoolExecutor
     */
    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        while (true) {
            // 判断线程池maxThread是否已满，是，则阻塞，否添加至队列。
            if (threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getMaximumPoolSize()) {
                try {
                    // 每次休息0.1秒，这是针对目前业务暂时取的值，可能会出现0.1秒的等待时间中存在时间浪费的问题。
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("线程池拒绝策略休眠唤醒异常");
                    e.printStackTrace();
                }
            } else {
                // 执行此任务
                threadPoolExecutor.submit(runnable);
                break;
            }
        }
    }
}