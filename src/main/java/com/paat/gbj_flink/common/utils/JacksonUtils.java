package com.paat.gbj_flink.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author binjie.gu
 * @version 1.0.0
 * @createTime 2021年06月15日
 */
@Slf4j
public class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Java 对象转为 JSON 字符串
     */
    public static <T> String toJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转为 Java 对象
     */
    public static <T> T toBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转为 Java-List 对象
     */
    public static <T> List<T> toBeanList(String jsonData, Class<T> type) {
        List<T> resultList;
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);

        try {
            resultList = objectMapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("JSON 转 Java-List 出错！", e);
            throw new RuntimeException(e);
        }

        return resultList;
    }

    /**
     * 将 JSON 字符串转为 Java-Map 对象
     */
    public static <K, V> Map<K, V> toMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        Map<K, V> resultMap;
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        try {
            resultMap = objectMapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultMap;
    }
}