import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * @author zhongyp.
 * @date 2020/3/16
 */
public class Test {


    /**
     * 归纳法，效率双100，适于用所有数字。
     * 思路很简单，分别归纳个位上1的个数，十位上1的个数，百位上1的个数等等，找出规律，我总结的规律如下：
     * <p>
     * 假如计算1～n中1出现的个数：
     * 变量说明：
     * j为10的倍数，j大小依据位数，比如个位时，j=10,十位j=100 等等。
     * y=n%j的余数；这里我们计算1的出现次数，所以w=1+1,如果计算的是2出现的次数，可以改为2+1，等等诸如此类。
     * 核心要点：计算某位上是1的数字个数时，可以先取整的，再处理剩余的，比如计算n=118时，计算十位上1的个数，
     * 我们可以先计算100以内十位上1的个数（10，11，...,18,19），我们看到有10个，再计算100-118十位上为1的数字，即8+1=9个。
     * <p>
     * 第一步：首先我们知道，个位数字为1的数字有：1, 11, 21, 31, ...,个位1的个数和n/10密切相关，
     * 也就是说有几个10，就有几个个位上是1的数字，则必然有(n/j)(j/10)个1，比如11/10=1， 则必然有1个1在个位（数字1个位上的1），
     * 21/10=2，则必然有2个1在个位（1，11两个数个位上的1)，118/100=1， 则必然有(n/100)(100/10)个十位上为1的数字（10，11，...,18,19）等等注入此类；
     * <p>
     * 第二步：对于118这个数字，假如我们计算十位上1的个数，我们算完第一步，已经知道了0～100区间，十位上是1的数字个数为10，
     * 并不知道100～118区间十位上是1的数字个数，所以我们只需计算110～118之间个位上是1的数字个数。余数y=118%j=18，j=100，
     * 首先18要大于等于10这个区间才会有符合要求的数字，否则没有，再者，如果18大于20，则有10，小于20，则有8+1=9个，
     * 即余数在j/10 <= y <j/10w，这个区间十位上是1的数字个数为y%(j/10)+1个，如118，余数区间：100/10 <= 18 < 100/102,
     * 则100-118区间十位为1的数字个数18%(100/10) + 1=9;如果y>= j/10*2，则这个区间十位上是1的数字个数为j/10个，如数字为128时，
     * 100~128这个区间十位上是1的数字个数为100/10=10个。
     */


    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
//        System.out.println(lengthOfLongestSubstring("tmmzuxt"));
        int[] a = new int[]{0, 2, 1, -6, 6, -7, 9, 1, 2, 0, 1};
        int[] b = new int[]{3, 3, 6, 5, -2, 2, 5, 1, -9, 4};
        int[] c = new int[]{12, -4, 16, -5, 9, -3, 3, 8, 0};
        System.out.println(canThreePartsEqualSum(c));


    }


    public static boolean canThreePartsEqualSum(int[] A) {
        int sum = 0, index = 0, index1 = 0, sum1 = 0, sum2 = 0;
        int len = A.length;
        for (int i = 0; i < len; i++) {
            sum += A[i];
        }
        if (sum % 3 != 0) {
            return false;
        }
        int avg = sum / 3;
        for (int i = 0; i < len; i++) {
            if (sum1 != avg) {
                sum1 += A[i];
            } else {
                index = index == 0 ? i - 1 : index;
            }
            int tempIndex = len - 1 - i;
            if (sum2 != avg) {
                if (index != 0) {
                    if (tempIndex > index) {
                        sum2 += A[tempIndex];
                    } else {
                        return false;
                    }
                } else {
                    if (tempIndex > i) {
                        sum2 += A[tempIndex];
                    } else {
                        return false;
                    }
                }

            } else {
                index1 = index1 == 0 ? len - i : index1;
            }
            if (sum1 == sum2 && sum1 == avg) {
                index1 = index1 == 0 ? len - i - 1 : index1;
                index = index == 0 ? i : index;
                break;
            }
        }
        int sum3 = 0;
        for (int j = index + 1; j < index1; j++) {
            sum3 += A[j];
        }
        if (sum3 == avg) {
            return true;
        } else {
            return false;
        }
//        return false;
    }


    public static int lengthOfLongestSubstring(String str) {
        if (str.length() == 0) {
            return 0;
        }
        int count = 0;
        int currentCount = 0;
        int startIndex = 0;
        int len = str.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            if (map.containsKey(str.charAt(i))) {
                if ((i - startIndex) > count) {
                    count = i - startIndex;
                }
                int index = map.get(str.charAt(i));
                if (startIndex <= index) {
                    startIndex = map.get(str.charAt(i)) + 1;
                    currentCount = i - startIndex + 1;
                } else {
                    currentCount++;
                }
                map.put(str.charAt(i), i);
            } else {
                map.put(str.charAt(i), i);
                currentCount++;
            }
        }
        return Math.max(count, currentCount);
    }

    public static void aamain(String[] args) throws Exception {

        Map<Character, Integer> map = new HashMap();


        Timer timer = new Timer();
//        timer.schedule(new LocalTimeTask(), 20, 10);
        System.out.println("我是别的任务");

//        for (int i = 0; i < 10; i++) {
        ScheduledFuture scheduledFuture = null;
        try {
            scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new LocalTask(), 0, 1000, TimeUnit.MILLISECONDS);
//                Thread.sleep(2000);
            System.out.println("结束了！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }

        System.out.println("任务分发完成喽！！！");

        scheduledFuture.get();
    }

    public static class LocalTimeTask implements Runnable {
        static volatile int num = 0;

        @Override
        @SneakyThrows
        public void run() {

            num++;
            if (num == 3) {
                throw new RuntimeException("异常");
            }
            Thread.sleep(1000);

            System.out.println("休眠结束！！！");
        }
    }

    public static class LocalTask implements Runnable {


        @Override
        public void run() {
//            while (true) {
            Future future = executorService.submit(new LocalTimeTask());
            try {
                future.get();
                Thread.sleep(5000);
                System.out.println("获取结果");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            }
        }
    }
}
