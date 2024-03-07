package com.yue.chip.core;

import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/7 下午6:14
 */
public interface PageSerializable<T> extends Page<T> , Serializable {
}
