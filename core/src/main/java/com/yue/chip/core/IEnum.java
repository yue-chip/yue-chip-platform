package com.yue.chip.core;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yue.chip.core.common.enums.Delete;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 枚举接口
 * @author mrliu
 *
 */
public interface IEnum {

	Integer getKey();

	String getName();

	String getDesc();

	@JsonValue
	public Map<String, Object> jsonValue();

}
