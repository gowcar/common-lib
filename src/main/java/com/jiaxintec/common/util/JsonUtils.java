package com.jiaxintec.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Class Name:  JsonUtils
 * Author:      Jacky Zhang
 * Create Time: 2020-03-08 下午11:18
 * Description:
 */
@Slf4j
public class JsonUtils
{
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static ObjectMapper objectMapperNotEmpty = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapperNotEmpty.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapperNotEmpty.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String jsonString, Class<T> valueType) {
        return readObjectValue(jsonString, valueType);
    }

    public static <T> T fromJson(String jsonString, TypeReference valueTypeRef) {
        return readObjectValue(jsonString, valueTypeRef);
    }

    public static String getJsonString(Object source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public static String objectToJson(Object source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public static String ArrayToJson(Object[] source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public static Object objectJsonToObject(String source) {
        try {
            return objectMapper.readValue(source, Object.class);
        } catch (java.io.IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Object[] ArrayJsonToArray(String source) {
        try {
            return objectMapper.readValue(source, Object[].class);
        } catch (java.io.IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static <T> T readObjectValue(String jsonString, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T readObjectValue(String jsonString, TypeReference valueTypeRef) {
        try {
            return objectMapper.readValue(jsonString, valueTypeRef);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String toStringNoEmpty(Object source){
        try {
            return objectMapperNotEmpty.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<Long> toLongList(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        return readObjectValue(jsonString, new TypeReference<List<Long>>() {
        });
    }
    public static Map<String, Object> pojoToMap(Object o) {

        return objectMapper.convertValue(o, new TypeReference<Map<String, Object>>(){});
    }
}
