package com.jiaxintec.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * @author bo
 */
public class JpaConverter implements AttributeConverter<Object, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object meta) {
        try {
            if (meta == null) {
                return null;
            }
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            if(StringUtils.isEmpty(dbData)) {
                return null;
            }
            return objectMapper.readValue(dbData, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
