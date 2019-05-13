package cn.cloudx.weichatsell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoggerTest {

    @Test
    public void test1() {
        String name = "imooc";
        String password = "12345";
        log.info("name:{},password:{}", name, password);
        log.info("log info");
        log.debug("log debug");
        log.warn("log warn");
        log.error("log error");
    }

}
