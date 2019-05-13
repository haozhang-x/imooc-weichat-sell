package cn.cloudx.weichatsell.handle;


import cn.cloudx.weichatsell.config.ProjectUrlConfig;
import cn.cloudx.weichatsell.exception.SellAuthorizeException;
import cn.cloudx.weichatsell.exception.SellException;
import cn.cloudx.weichatsell.utils.ResultVOUtils;
import cn.cloudx.weichatsell.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class SellExceptionHandler {

    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    public SellExceptionHandler(ProjectUrlConfig projectUrlConfig) {
        this.projectUrlConfig = projectUrlConfig;
    }

    /**
     * 拦截登录异常
     */
    @ExceptionHandler(value = SellAuthorizeException.class)
    public ModelAndView handleSellAuthorizeException() {
        String stringBuilder = "redirect:" + projectUrlConfig.getSell() +
                "/seller/order/list";
        return new ModelAndView(stringBuilder);
    }


    /**
     * 拦截sellException异常
     *
     * @param e e
     * @return ResultVO
     */

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public ResultVO handlerSellException(SellException e) {
        return ResultVOUtils.error(e.getCode(), e.getMessage());
    }
}
