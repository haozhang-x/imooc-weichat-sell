package cn.cloudx.weichatsell.enums;

import lombok.Getter;

/**
 * 商品状态
 *
 * @author zhanghao
 * @date 2018/04/20
 */
@Getter
public enum ProductStatusEnum implements CodeEnum {
    /**
     * 在架
     */
    UP(0, "在架"),
    /**
     * 下架
     */
    DOWN(1, "下架");
    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
