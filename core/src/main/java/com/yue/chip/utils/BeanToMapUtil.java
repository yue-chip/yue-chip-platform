package com.yue.chip.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * java bean 转 map
 * 
 * @author mrliu
 *
 */
public class BeanToMapUtil {

	/**
	 * java bean 转 map
	 * 
	 * @param bean
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> transBeanToMap(final Object bean) {
		if(Objects.isNull(bean)){
			return Collections.EMPTY_MAP;
		}
		return transBeanToMap(new HashMap<String, Object>(), bean);
	}

	/**
	 * java bean 转 map
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @param bean
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> transBeanToMap(final Map<String, Object> map,final Object bean) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new ParameterNamesModule())
			.registerModule(new Jdk8Module())
			.registerModule(new JavaTimeModule());
		String josn = "";
		try {
			josn = mapper.writeValueAsString(bean);
			Map<String, Object> mapTmp = mapper.readValue(josn, HashMap.class);
			map.putAll(mapTmp);
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		return map;
	}
}
