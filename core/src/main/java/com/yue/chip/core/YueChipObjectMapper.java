package com.yue.chip.core;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Mr.Liu
 *
 */
@Component
public class YueChipObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7300608107391001448L;


	public YueChipObjectMapper() {
		super();
		// 允许单引号
		this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		// 字段和值都加引号
		this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		registerModule(simpleModule);
//		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 空值处理为空串
//		this.getSerializerProvider()
//				.setNullValueSerializer(new com.fasterxml.jackson.databind.JsonSerializer<Object>() {
//					@Override
//					public void serialize(Object arg0, com.fasterxml.jackson.core.JsonGenerator arg1,
//							com.fasterxml.jackson.databind.SerializerProvider arg2)
//							throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
//						if (arg0 instanceof List || arg0 instanceof ArrayList) {
//							arg1.writeEndArray();
//						} else if (arg0 instanceof Map || arg0 instanceof HashMap) {
//							arg1.writeEndObject();
//						} else if (arg0 instanceof String) {
//							arg1.writeString("");
//						} else {
//							arg1.writeString("");
//						}
//					}
//				});

	}

	private static class Holder{
		private static YueChipObjectMapper instance = new YueChipObjectMapper();
	}

	public static YueChipObjectMapper getInstance(){
		return Holder.instance;
	}
}
