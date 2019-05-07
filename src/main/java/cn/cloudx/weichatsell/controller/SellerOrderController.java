package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 卖家端注解
 *
 * @author zhanghao
 * @date 2018/05/01
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {
    private OrderService orderService;

    @Autowired
    public SellerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 订单列表
     *
     * @param page 第几页,从第一页开始
     * @param size 一页有多少数据
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size, ModelMap modelMap) {
        Page<OrderDTO> orderDTOPage = orderService.findList(PageRequest.of(page - 1, size));
        modelMap.put("orderDTOPage", orderDTOPage);
        modelMap.put("currentPage", page);
        modelMap.put("size", size);
        return new ModelAndView("order/list", modelMap);
    }


    /**
     * 取消订单
     *
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId, ModelMap modelMap) {
        OrderDTO orderDTO;
        try {
            orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        } catch (SellException e) {
            log.error("[卖家端取消订单]发生异常{}", e);
            modelMap.put("msg", e.getMessage());
            modelMap.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error", modelMap);
        }
        modelMap.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        modelMap.put("url", "/sell/seller/order/list");
        return new ModelAndView("/common/success", modelMap);
    }


    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId, ModelMap modelMap) {
        OrderDTO orderDTO;
        try {
            orderDTO = orderService.findOne(orderId);
        } catch (SellException e) {
            log.error("[卖家端查询订单详情]发生异常{}", e);
            modelMap.put("msg", e.getMessage());
            modelMap.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error", modelMap);
        }
        modelMap.put("orderDTO", orderDTO);
        return new ModelAndView("/order/detail", modelMap);
    }


    /**
     * 完结订单
     */
    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId, ModelMap modelMap) {
        OrderDTO orderDTO;
        try {
            orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        } catch (SellException e) {
            log.error("[卖家端完结订单]发生异常{}", e);
            modelMap.put("msg", e.getMessage());
            modelMap.put("url", "/sell/seller/order/list");
            return new ModelAndView("/common/error", modelMap);
        }
        modelMap.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        modelMap.put("url", "/sell/seller/order/list");
        return new ModelAndView("/common/success", modelMap);
    }
}
