package cn.cloudx.weichatsell.dataobject;


import cn.cloudx.weichatsell.enums.ProductStatusEnum;
import cn.cloudx.weichatsell.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品信息
 *
 * @author zhanghao
 * @date 2018.4.20
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo {

    @Id
    private String productId;
    /**
     * 名字
     */
    private String productName;
    /**
     * 单价
     */
    private BigDecimal productPrice;

    /**
     * 库存
     */
    private Integer productStock;
    /**
     * 描述
     */
    private String productDescription;
    /**
     * 小图
     */
    private String productIcon;

    /**
     * 状态 0正常 1下架
     */
    private Integer productStatus = ProductStatusEnum.UP.getCode();

    /**
     * 类目编号
     */

    private Integer categoryType;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;


    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }


}
