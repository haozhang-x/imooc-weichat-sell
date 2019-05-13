package cn.cloudx.weichatsell.repository;

import cn.cloudx.weichatsell.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository repository;


    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456");
        orderDetail.setOrderId("12344");
        orderDetail.setProductIcon("xxxx/jpg");
        orderDetail.setProductId("123");
        orderDetail.setProductName("hahah");
        orderDetail.setProductPrice(new BigDecimal(10));
        orderDetail.setProductQuantity(10);
        OrderDetail save = repository.save(orderDetail);
        Assert.assertNotNull(save);
    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetailList = repository.findByOrderId("123");
        Assert.assertNotEquals(0, orderDetailList.size());
    }
}