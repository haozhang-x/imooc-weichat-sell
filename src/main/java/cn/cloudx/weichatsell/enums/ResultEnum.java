package cn.cloudx.weichatsell.enums;

import lombok.Getter;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    PRODUCT_NOT_EXISTS(10, "商品不存在"),

    PRODUCT_STOCK_ERROT(20, "库存不正确"),

    ORDER_NOT_EXISTS(30, "订单不存在"),

    ORDER_DETAIL_NOT_EXISTS(40, "订单详情不存在"),

    ORDER_STATUS_ERROR(50, "订单状态不正确"),

    ORDER_UPDATE_FAIL(60, "订单更新失败"),

    ORDER_DETAIL_EMPTY(70, "订单中无商品"),

    ORDER_PAY_STATUS_ERROR(80, "支付状态不正确"),

    CART_EMPTY(90, "购物车为空"),

    ORDER_OWNER_ERROR(100, "该订单不属于当前用户"),


    WECHAT_MP_ERROR(101, "微信公众账号方面错误"),

    WECHAT_PAY_MONEY_VERITY_ERROR(102, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(103, "订单取消成功"),

    ORDER_FINISH_SUCCESS(104, "订单完结成功"),


    PRODUCT_STATUS_ERROR(105, "商品状态不正确"),


    LOGIN_FAIL(106, "登录失败,登录信息不正确"),


    LOGOUT_SUCCESS(107, "登出成功");


    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
