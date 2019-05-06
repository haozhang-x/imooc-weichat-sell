package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhanghao
 * @date 2018/04/27
 */
@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {

    private OrderService orderService;
    private PayService payService;

    @Autowired
    public PayController(OrderService orderService, PayService payService) {
        this.orderService = orderService;
        this.payService = payService;
    }


    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               ModelMap modelMap) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXISTS);
        }
        orderDTO.setBuyerOpenid("oTgZpwX4spaRFEKBvZgMc1IrqUzg");
        PayResponse payResponse = payService.create(orderDTO);
        modelMap.put("payResponse", payResponse);
        modelMap.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", modelMap);
    }


    @PostMapping("notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);
        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }

}
