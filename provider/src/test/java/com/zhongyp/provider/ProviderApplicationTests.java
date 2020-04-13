package com.zhongyp.provider;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProviderApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(countDigitOne(824883294));
    }

    public int countDigitOne(int n) {

        int result = 0;
        long temp = n, j = 10;
        while (temp * 10 >= j) {
            long y = temp % j;
            long z = temp / j;
            result += (z * j / 10);
            if (y >= (j / 10)) {
                if (y < (j * 2 / 10)) {
                    y = y % (j / 10);
                    result += y + 1;
                } else {
                    result += (j / 10);
                }
            }

            j *= 10;

        }

        return result;

    }

}
