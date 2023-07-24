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
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	/**
	 * 抛自定义异常
	 * @param message
	 */
	public static void throwException(String message){
		throw new BusinessException(message);
	}
}
