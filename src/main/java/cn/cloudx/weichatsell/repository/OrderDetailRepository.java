package cn.cloudx.weichatsell.repository;


import cn.cloudx.weichatsell.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/21
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    /**
     * 通过OrderId查询订单详情表
     *
     * @param orderId 订单id
     * @return List<OrderDetail>
     */
    List<OrderDetail> findByOrderId(String orderId);
}
