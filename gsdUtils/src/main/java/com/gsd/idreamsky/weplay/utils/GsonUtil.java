package com.gsd.idreamsky.weplay.utils;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jove.chen on 2017/3/30.
 */

public class GsonUtil {

    /**
     * 解析json数组
     *
     * @param list 需要保存到的list
     * @param clazz 解析类
     * @param jsonArrayStr 被解析的json string
     * @return 当前解析出来的数据个数
     */
    public static <T> int parseJsonArray(List<T> list, Class<T> clazz, String jsonArrayStr) {
        if (TextUtils.isEmpty(jsonArrayStr)) {
            return 0;
        }
        List<T> list2 = parseJsonArray(clazz, jsonArrayStr);
        list.addAll(list2);
        return list2.size();
    }

    public static <T> List<T> parseJsonArray(Class<T> clazz, String jsonArrayStr) {
        List<T> list = new ArrayList<>();
        if (TextUtils.isEmpty(jsonArrayStr)) {
            return list;
        }

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonArrayStr);
        if (!element.isJsonArray()){//不是jsonArray 则返回
            return list;
        }

        JsonArray jsonArray = element.getAsJsonArray();
        for (JsonElement one : jsonArray) {
            T msg = gson.fromJson(one, clazz);
            list.add(msg);
        }
        return list;
    }
}
