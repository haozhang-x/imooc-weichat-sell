package cn.cloudx.weichatsell.enums;

import lombok.Getter;


@Getter
public enum PayStatusEnum implements CodeEnum {

    /**
     * 未支付
     */
    WAIT(0, "等待支付"),
    /**
     * 支付成功
     */
    SUCCESS(1, "支付成功"),
    ;


    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
