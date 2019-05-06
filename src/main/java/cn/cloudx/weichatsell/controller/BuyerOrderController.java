package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.converter.OrderForm2OrderDTOConverter;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.service.BuyerService;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.converter.OrderForm2OrderDTOConverter;
import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.form.OrderForm;
import cn.cloudx.weichatsell.service.BuyerService;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.utils.ResultVOUtils;
import cn.cloudx.weichatsell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghao
 * @date 2018/04/24
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    private OrderService orderService;
    private BuyerService buyerService;

    @Autowired
    public BuyerOrderController(OrderService orderService, BuyerService buyerService) {
        this.orderService = orderService;
        this.buyerService = buyerService;
    }


    /**
     * 创建订单
     */
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("[创建订单]参数不正确,orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.covert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[创建订单]购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createOrderDTO = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", createOrderDTO.getOrderId());


        return ResultVOUtils.success(map);

    }

    /**
     * 订单列表
     */

    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, PageRequest.of(page, size));
        List<OrderDTO> orderDTOList = orderDTOPage.getContent();

        return ResultVOUtils.success(orderDTOList);

    }


    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单详情] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtils.success(orderDTO);
    }

    /**
     * 取消订单
     */

    @RequestMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单详情] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtils.success();
    }


}
