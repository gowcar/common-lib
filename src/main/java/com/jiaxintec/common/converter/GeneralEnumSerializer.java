package com.jiaxintec.common.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Class Name:  GeneralEnumSerializer
 * Author:      Jacky Zhang
 * Create Time: 2019/9/23 下午6:13
 * Description:
 */
public class GeneralEnumSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String fName = StringUtils.remove(o.getClass().getSimpleName(),"Enum");
        fName = fName.substring(0, 1).toLowerCase() + fName.substring(1, fName.length());
        jsonGenerator.writeString(((EnumDescriptor) o).name());
        jsonGenerator.writeStringField(fName + "Desc", ((EnumDescriptor) o).getDesc());
    }
}
