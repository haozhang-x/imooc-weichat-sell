package cn.cloudx.weichatsell.service.impl;

import cn.cloudx.weichatsell.converter.OrderMaster2OrderDTOConverter;
import cn.cloudx.weichatsell.dataobject.OrderDetail;
import cn.cloudx.weichatsell.dataobject.OrderMaster;
import cn.cloudx.weichatsell.dataobject.ProductInfo;
import cn.cloudx.weichatsell.dto.CartDTO;
import cn.cloudx.weichatsell.dto.OrderDTO;
import cn.cloudx.weichatsell.enums.OrderStatusEnum;
import cn.cloudx.weichatsell.enums.PayStatusEnum;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.repository.OrderDetailRepository;
import cn.cloudx.weichatsell.repository.OrderMasterRepository;
import cn.cloudx.weichatsell.service.OrderService;
import cn.cloudx.weichatsell.service.PayService;
import cn.cloudx.weichatsell.service.ProductService;
import cn.cloudx.weichatsell.service.WebSocket;
import cn.cloudx.weichatsell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private ProductService productService;
    private OrderDetailRepository orderDetailRepository;
    private OrderMasterRepository orderMasterRepository;
    private PayService payService;
    private WebSocket webSocket;

    @Autowired
    public OrderServiceImpl(ProductService productService, OrderDetailRepository orderDetailRepository, OrderMasterRepository orderMasterRepository, PayService payService, WebSocket webSocket) {
        this.productService = productService;
        this.orderDetailRepository = orderDetailRepository;
        this.orderMasterRepository = orderMasterRepository;
        this.payService = payService;
        this.webSocket = webSocket;
    }


    /**
     * 创建订单
     *
     * @param orderDTO orderDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        //1.查询商品(数量、价格)
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            //2.计算订单总价
            orderAmount = productInfo.getProductPrice().
                    multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);

        }

        //3.写入订单数据库(orderMaster和orderDetail)

        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4.扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);


        //发送websocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    /**
     * 查询单个订单
     *
     * @param orderId orderId
     */
    @Override
    public OrderDTO findOne(String orderId) {
        Optional<OrderMaster> orderMaster = orderMasterRepository.findById(orderId);
        if (!orderMaster.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXISTS);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster.get(), orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    /**
     * 查询订单列表
     *
     * @param buyerOpenId
     * @param pageable
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    /**
     * 取消订单
     *
     * @param orderDTO orderDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[取消订单]订单状态不正确,orderId={},orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if (updatedOrderMaster == null) {
            log.error("[取消订单]更新失败,orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[取消订单]订单中无商品详情,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(
                e -> new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);


        //如果已支付,需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
        }


        return orderDTO;
    }

    /**
     * 完结订单
     *
     * @param orderDTO orderDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[完结订单] 订单状态不正确,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if (updatedOrderMaster == null) {
            log.error("[完结订单] 更新失败,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    /**
     * 支付订单
     *
     * @param orderDTO orderDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[支付订单] 订单状态不正确,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("[支付订单] 支付状态不正确,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updatedOrderMaster = orderMasterRepository.save(orderMaster);
        if (updatedOrderMaster == null) {
            log.error("[支付订单] 更新失败,orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    /**
     * 查询订单列表
     *
     * @param pageable
     */
    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
