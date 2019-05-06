package cn.cloudx.weichatsell.dto;

import lombok.Data;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@Data
public class CartDTO {
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 商品数量
     */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
