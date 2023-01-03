package com.yue.chip.core.persistence.curd;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface SaveRepository<T> {


	/**
	 * 使用jpql新增数据
	 * @param jpql	jpql插入语句
	 * @return				int
	 */
	@Transactional(rollbackFor = Throwable.class)
	int save(String jpql);
	
	/**
	 * 使用jpql新增数据
	 * @param jpql	jpql插入语句
	 * @param parameter			插入数据
	 * @return				int
	 */
	@Transactional(rollbackFor = Throwable.class)
	int save(String jpql, Map<String, Object> parameter);
		
}
