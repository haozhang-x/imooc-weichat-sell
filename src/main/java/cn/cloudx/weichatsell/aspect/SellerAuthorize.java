package cn.cloudx.weichatsell.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class SellerAuthorize {

    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    public SellerAuthorize(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Pointcut("execution(public * cn.cloudx.weichatsell.controller.Seller*.*(..))" +
            "&&!execution(public * cn.cloudx.weichatsell.controller.SellerUserController.*(..))")
    public void verify() {
    }

    /*@Before("verify()")
    public void doVerify() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie == null) {

            log.warn("[登录校验]cookie中查不到token");
            throw new SellAuthorizeException();
        }

        //去redis里面查
        String token = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (StringUtils.isEmpty(token)){
            log.warn("[登录校验]Redis中查不到token");
            throw new SellAuthorizeException();
        }
    }*/
}

