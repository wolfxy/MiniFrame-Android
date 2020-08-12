package org.mini.frame.toolkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Wuquancheng on 15/4/3.
 */
public class MiniJsonUtil {

    public static String toJsonString(Object object) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(object);
    }

    public static <T> T stringToObject(String jsonString, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonString,clazz);
    }

    public static <T> List<T> stringToList(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type type = new TypeToken<List<T>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
