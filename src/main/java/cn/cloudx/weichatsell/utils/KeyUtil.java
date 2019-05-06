package cn.cloudx.weichatsell.utils;

import java.util.Random;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
public class KeyUtil {
    /**
     * 生成唯一的主键
     * 格式:时间+随机数
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer randomNumber = random.nextInt(900000) + 10000;
        return System.currentTimeMillis() + String.valueOf(randomNumber);
    }
}
