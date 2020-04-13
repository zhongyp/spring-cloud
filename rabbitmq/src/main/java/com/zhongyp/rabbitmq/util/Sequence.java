package com.zhongyp.rabbitmq.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/19
 * @since jdk1.8
 */
public class Sequence {

    private static AtomicInteger sequence = new AtomicInteger(0);

    public static int getSequence() {

        if (sequence.get() >= 1000) {
            sequence.set(0);
        }
        return sequence.incrementAndGet();
    }
}
