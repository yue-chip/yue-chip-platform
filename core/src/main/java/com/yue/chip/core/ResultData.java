package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yue.chip.constant.ResultDataConstant;
import com.yue.chip.core.common.enums.ResultDataState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 返回给客户端的结果集
 *
 * @author mrliu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
@Schema()
@Builder
public class ResultData<T> implements Serializable, IResultData<T> {

	private static final long serialVersionUID = 981792735336739260L;

	@Builder.Default
	@Schema(description = "返回消息", type="string")
	private String message = ResultDataConstant.SUCCEED_MESSAGE;

	@Schema(description = "异常消息", type="string")
	private String exceptionMessage;

	@Builder.Default
	@Schema(description = "状态编码",type="integer")
	private Integer status = ResultDataState.SUCCESS.getKey();

	@Schema(description = "结果集", type="object")
	private T data;


	public IResultData<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static ResultData succeed(String message){
		return ResultData.builder()
				.status(ResultDataState.ERROR.getKey())
				.message(message)
				.build();
	}

	public static ResultData succeed(){
		return succeed(ResultDataConstant.SUCCEED_MESSAGE);
	}

	public static ResultData failed(String message){
		return failed(ResultDataState.ERROR.getKey(),message);
	}

	public static ResultData failed(int state, String message){
		return ResultData.builder()
				.status(state)
				.exceptionMessage(message)
				.build();
	}

	public static ResultData failed(){
		return failed(ResultDataConstant.FAILED_MESSAGE);
	}


}
