package cn.cloudx.weichatsell.utils;

import cn.cloudx.weichatsell.vo.ResultVO;

/**
 * @author zhanghao
 * @date 2018/04/21
 */
public class ResultVOUtils {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
