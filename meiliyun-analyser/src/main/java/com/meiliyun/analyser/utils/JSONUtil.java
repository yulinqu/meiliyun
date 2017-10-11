package com.meiliyun.analyser.utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

    public final static String DEFAULT_DATE_FORMAT = "MMM d, yyyy h:mm:ss a";

    private static Gson gsonDefault = new Gson();

    private static Gson onlyIncludeExposeFieldGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    private static Map<String, ObjectMapper> dateFormatToGsonMap = new ConcurrentHashMap<String, ObjectMapper>();
    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        dateFormatToGsonMap.put(DEFAULT_DATE_FORMAT, objectMapper);
    }

    public static <T> T fromJson(String json, TypeReference typeReference) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return dateFormatToGsonMap.get(DEFAULT_DATE_FORMAT).readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Convert json string to object failed, input string is: " + json, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return dateFormatToGsonMap.get(DEFAULT_DATE_FORMAT).readValue(json, clz);
        } catch (Exception e) {
            throw new RuntimeException("Convert json string to object failed, input string is: " + json, e);
        }
    }

    public static <T> T fromJson(String json, Class collectionClass, Class elementClasses) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            JavaType javaType = dateFormatToGsonMap.get(DEFAULT_DATE_FORMAT).getTypeFactory()
                    .constructCollectionType(collectionClass, elementClasses);
            return dateFormatToGsonMap.get(DEFAULT_DATE_FORMAT).readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Convert json string to object failed, input string is: " + json, e);
        }
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz, String dateFormat) {

        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        ObjectMapper objectMapper = dateFormatToGsonMap.get(dateFormat);
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
            dateFormatToGsonMap.put(dateFormat, objectMapper);
        }

        try {
            return dateFormatToGsonMap.get(dateFormat).readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Convert json string to list object failed, input string is: " + jsonStr, e);
        }
    }

    public static String toJsonOnlyForExposeFields(Object object) {
        return onlyIncludeExposeFieldGson.toJson(object);
    }

    public static String toJsonStr(Object src) {
        return gsonDefault.toJson(src);
    }

    public static String toJsonStrWithType(Object src, Type objType) {
        return gsonDefault.toJson(src, objType);
    }

}
