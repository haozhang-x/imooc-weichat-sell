package cn.cloudx.weichatsell.dto;

import cn.cloudx.weichatsell.dataobject.OrderDetail;
import cn.cloudx.weichatsell.enums.OrderStatusEnum;
import cn.cloudx.weichatsell.enums.PayStatusEnum;
import cn.cloudx.weichatsell.utils.EnumUtil;
import cn.cloudx.weichatsell.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    /**
     * 订单详情
     */
    List<OrderDetail> orderDetailList;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 买家名字
     */
    private String buyerName;
    /**
     * 买家联系方式
     */
    private String buyerPhone;
    /**
     * 买家地址
     */
    private String buyerAddress;
    /**
     * 买家openId
     */
    private String buyerOpenid;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;
    /**
     * 订单状态默认状态为新订单
     */
    private Integer orderStatus;
    /**
     * 订单支付状态 默认未支付
     */
    private Integer payStatus;
    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }

}
