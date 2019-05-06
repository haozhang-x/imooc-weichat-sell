package cn.cloudx.weichatsell.repository;

import cn.cloudx.weichatsell.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author zhanghao
 * @date 2018/04/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
    public final String BUYER_OPENID = "123";

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void findOneTest() {
    }

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12233");
        orderMaster.setBuyerName("张浩");
        orderMaster.setBuyerAddress("北京市昌平区");
        orderMaster.setBuyerPhone("123456789101");
        orderMaster.setBuyerOpenid("123");
        orderMaster.setOrderAmount(new BigDecimal(4.5));
        OrderMaster save = repository.save(orderMaster);
        Assert.assertNotNull(save);
    }

    @Test
    public void findAllTest() {
    }

    @Test
    public void findByBuyerOpenid() {
        Page<OrderMaster> orderMasters = repository.findByBuyerOpenid(BUYER_OPENID, PageRequest.of(0, 3));
        Assert.assertNotEquals(0, orderMasters.getTotalPages());

    }
}