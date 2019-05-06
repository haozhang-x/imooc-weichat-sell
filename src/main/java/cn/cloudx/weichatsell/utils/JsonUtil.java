package cn.cloudx.weichatsell.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author zhanghao
 * @date 2018/04/27
 */
public class JsonUtil {
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
