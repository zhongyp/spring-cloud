package com.zhongyp.rabbitmq.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/4/1
 * @since jdk1.8
 */
public class TranserDTO implements Serializable {

    String a;
    String b;
    String c;
    int d;
    List s;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public List getS() {
        return s;
    }

    public void setS(List s) {
        this.s = s;
    }

}
