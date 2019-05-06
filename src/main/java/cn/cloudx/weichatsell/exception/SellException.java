package cn.cloudx.weichatsell.exception;

import cn.cloudx.weichatsell.enums.ResultEnum;
import lombok.Getter;

/**
 * @author zhanghao
 * @date 2018/04/22
 */
@Getter
public class SellException extends RuntimeException {
    private Integer code;


    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }


    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
