package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.service.PayService;
import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanghao
 * @date 2018/04/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayServiceImplTest {


    @Autowired
    private PayService payService;


    @Autowired
    private OrderService orderService;

    @Test
    public void create() {
        OrderDTO orderDTO = orderService.findOne("1524579831908151759");
        payService.create(orderDTO);
    }
}