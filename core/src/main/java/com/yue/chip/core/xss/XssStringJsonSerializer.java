package com.yue.chip.core.xss;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (s != null) {
            String encodedValue = StringEscapeUtils.escapeHtml4(s);
            jsonGenerator.writeString(encodedValue);
        }
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }
}
