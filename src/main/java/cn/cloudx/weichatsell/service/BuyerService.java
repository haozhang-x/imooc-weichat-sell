package cn.cloudx.weichatsell.service;


import cn.cloudx.weichatsell.dto.OrderDTO;


public interface BuyerService {
    /**
     * 查询一个订单
     *
     * @return OrderDTO
     */
    OrderDTO findOrderOne(String openid, String orderId);

    /**
     * 取消订单
     */

    OrderDTO cancelOrder(String openid, String orderId);

}
