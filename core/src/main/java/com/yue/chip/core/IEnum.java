package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * 枚举接口
 * @author mrliu
 *
 */
public interface IEnum {

	default String code(){
		return UUID.randomUUID().toString();
	}

	default String dataVersion(){
		return "1";
	}

	Integer getKey();

	String getName();

	String getDesc();

	@JsonValue
	Object jsonValue();

}
