package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yue.chip.constant.ResultDataConstant;
import com.yue.chip.core.common.enums.ResultDataState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

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
public class ResultData implements Serializable, IResultData {

	private static final long serialVersionUID = 981792735336739260L;

	@Builder.Default
	private String message = ResultDataConstant.SUCCEED_MESSAGE;

	private String exceptionMessage;

	@Builder.Default
	private Integer status = ResultDataState.SUCCESS.getKey();

	private Object data;

	private String traceId;

	public String getTraceId() {
		return TraceContext.traceId();
	}

	public IResultData setData(Object data) {
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
		return failed(ResultDataState.ERROR.getKey(),message,null);
	}
	public static ResultData failed(String message,String exceptionMessage){
		return failed(ResultDataState.ERROR.getKey(),message,exceptionMessage);
	}

	public static ResultData failed(int state, String message,String exceptionMessage){
		return ResultData.builder()
				.status(state)
				.message(message)
				.exceptionMessage(exceptionMessage)
				.build();
	}

	public static ResultData failed(){
		return failed(ResultDataConstant.FAILED_MESSAGE);
	}



}
