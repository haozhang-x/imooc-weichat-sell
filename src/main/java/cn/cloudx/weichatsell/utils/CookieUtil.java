package cn.cloudx.weichatsell.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie工具类
 *
 * @author zhanghao
 * @date 2018/05/06
 */
public class CookieUtil {


    /**
     * 获取cookie
     *
     * @param request request
     * @param name    name
     * @return Cookie
     */
    public static Cookie get(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        return cookieMap.getOrDefault(name, null);
    }


    /**
     * 设置cookie
     *
     * @param response response
     * @param name     name
     * @param value    value
     * @param maxAge   maxAge
     */
    public static void set(HttpServletResponse response, String name, String value, int maxAge) {

        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 封装cookie为map
     *
     * @param request
     * @return
     */

    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}
