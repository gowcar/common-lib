package com.jiaxintec.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

/**
 * @author bo
 */
public class JpaListConverter<T> implements AttributeConverter<List<T>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<T> meta) {
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
    public List<T> convertToEntityAttribute(String dbData) {
        try {
            if(StringUtils.isEmpty(dbData)) {
                return null;
            }
            return objectMapper.readValue(dbData, new TypeReference<List<T>>(){} );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
