package cn.cloudx.weichatsell.dataobject;


import cn.cloudx.weichatsell.enums.OrderStatusEnum;
import cn.cloudx.weichatsell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author zhang
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    /**
     * 订单ID
     */
    @Id
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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
    /**
     * 订单支付状态 默认未支付
     */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;


}
