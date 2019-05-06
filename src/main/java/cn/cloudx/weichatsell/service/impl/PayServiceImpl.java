package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.service.PayService;
import cn.cloudx.weichatsell.utils.JsonUtil;
import cn.cloudx.weichatsell.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author zhanghao
 * @date 2018/04/27
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信点餐订单";
    private BestPayService bestPayService;
    private OrderService orderService;

    @Autowired
    @Lazy
    public PayServiceImpl(BestPayService bestPayService, OrderService orderService) {
        this.bestPayService = bestPayService;
        this.orderService = orderService;
    }


    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        // payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOpenid("oTgZpwX4spaRFEKBvZgMc1IrqUzg");
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("[微信支付] 发起支付request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("[微信支付] 发起支付response={}", JsonUtil.toJson(payResponse));
        return payResponse;

    }

    @Override
    public PayResponse notify(String notifyData) {
        //1.验证签名
        //2.支付状态
        //3.支付的金额
        //4.支付人(下单人==支付人)
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("[微信支付]异步通知,payResponse={}", JsonUtil.toJson(payResponse));
        String orderId = payResponse.getOrderId();
        OrderDTO orderDTO = orderService.findOne(orderId);
        //判断订单是否存在
        if (orderDTO == null) {
            log.error("[微信支付]异步通知,订单不存在 orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXISTS);
        }
        Double orderAmount = payResponse.getOrderAmount();

        if (!MathUtil.equals(payResponse.getOrderAmount(), orderDTO.getOrderAmount().doubleValue())) {
            log.error("[微信支付]异步通知,订单金额不一致 orderId={},微信通知金额={},系统金额={}", orderId, orderAmount, orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WECHAT_PAY_MONEY_VERITY_ERROR);
        }


        //修改订单的支付状态
        orderService.paid(orderDTO);
        return payResponse;
    }


    /**
     * 退款
     *
     * @param orderDTO
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("[微信退款] request={}", JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("[微信退款] response={}", JsonUtil.toJson(refundResponse));
        return refundResponse;
    }
}
