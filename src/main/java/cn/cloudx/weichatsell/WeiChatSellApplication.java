package cn.cloudx.weichatsell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author zhanghao
 * @date 2018/4/14
 */
@SpringBootApplication
@EnableCaching
public class WeiChatSellApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiChatSellApplication.class, args);
    }
}
