package cn.cloudx.weichatsell.controller;

import cn.cloudx.weichatsell.constant.CookieConstant;
import cn.cloudx.weichatsell.constant.RedisConstant;
import cn.cloudx.weichatsell.dataobject.SellerInfo;
import cn.cloudx.weichatsell.service.SellerService;
import cn.cloudx.weichatsell.config.ProjectUrlConfig;
import cn.cloudx.weichatsell.constant.CookieConstant;
import cn.cloudx.weichatsell.constant.RedisConstant;
import cn.cloudx.weichatsell.dataobject.SellerInfo;
import cn.cloudx.weichatsell.enums.ResultEnum;
import cn.cloudx.weichatsell.service.SellerService;
import cn.cloudx.weichatsell.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao
 * @date 2018/05/06
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {
    private SellerService sellerService;
    private StringRedisTemplate stringRedisTemplate;
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    public SellerUserController(SellerService sellerService, StringRedisTemplate stringRedisTemplate, ProjectUrlConfig projectUrlConfig) {
        this.sellerService = sellerService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.projectUrlConfig = projectUrlConfig;
    }


    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid, HttpServletResponse response, ModelMap modelMap) {
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        //1.openid 去和数据库比较
        if (sellerInfo == null) {
            modelMap.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            modelMap.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", modelMap);
        }
        //2.设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);

        //3.设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.EXPIRE);
        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/seller/order/list");
    }


    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        //1.从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2.清除redis
            stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
            //3.清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }
        modelMap.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        modelMap.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", modelMap);
    }

}
