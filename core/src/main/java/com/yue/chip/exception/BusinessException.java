package com.yue.chip.exception;

import java.io.Serializable;

/**
 * 异常处理类
 *
 * @author mrliu
 *
 */
public class BusinessException extends  RuntimeException implements Serializable {

	private static final long serialVersionUID = 7670720348542447804L;

	public BusinessException() {
	}

	public BusinessException(Exception e) {
		super(e);
		throw this;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		throw this;
	}

	public BusinessException(Throwable cause) {
		super(cause);
		throw this;
	}

	public BusinessException(String message) {
		super(message);
		throw this;
	}

	/**
	 * 抛自定义异常
	 * @param message
	 */
	public static void throwException(String message){
		new BusinessException(message);
	}
}
