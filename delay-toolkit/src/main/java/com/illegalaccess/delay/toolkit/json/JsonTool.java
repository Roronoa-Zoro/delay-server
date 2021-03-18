package com.illegalaccess.delay.toolkit.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * json序列化和反序列化工具
 *
 * @author Jimmy Li
 * @date 2021-02-02 17:51
 */
public class JsonTool {

    private JsonTool() {
        throw new RuntimeException("can not create json tool");
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 序列化成json字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化为对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clz) {
        try {
            return objectMapper.readValue(json, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化为对象集合
     *
     * @param jsonArray
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String jsonArray, Class<T> clz) {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clz);
        try {
            return objectMapper.readValue(jsonArray, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
