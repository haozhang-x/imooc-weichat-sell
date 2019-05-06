package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.dataobject.OrderDetail;
import cn.cloudx.weichatsell.utils.JsonUtil;
import cn.cloudx.weichatsell.dataobject.OrderDetail;
import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.OrderStatusEnum;
import cn.cloudx.weichatsell.enums.PayStatusEnum;
import cn.cloudx.weichatsell.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    private final String BUYER_OPENID = "1111";
    private final String ORDER_ID = "1524403175684785967";
    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress("慕课网");
        orderDTO.setBuyerPhone("1234567");
        orderDTO.setBuyerOpenid(BUYER_OPENID);
        orderDTO.setBuyerName("我");

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123456");
        o1.setProductQuantity(10);
        orderDetailList.add(o1);
        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("[创建订单] result={}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        log.info("[查询单个订单]{}", orderDTO);
        Assert.assertNotNull(orderDTO);

    }

    @Test
    public void findList() {
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID, PageRequest.of(0, 3));
        Assert.assertNotEquals(0, orderDTOPage.getTotalPages());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO retOrderDTO = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), retOrderDTO.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO retOrderDTO = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISH.getCode(), retOrderDTO.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO retOrderDTO = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), retOrderDTO.getPayStatus());
    }

    @Test
    public void findListTest() {
        Page<OrderDTO> orderDTOPage = orderService.findList(PageRequest.of(0, 10));
//        Assert.assertNotEquals(0,orderDTOPage.getContent().size());
        Assert.assertTrue("查询所有的订单列表", orderDTOPage.getTotalElements() < 0);
        log.info("orderDTOPage={}", JsonUtil.toJson(orderDTOPage.getContent()));
    }
}