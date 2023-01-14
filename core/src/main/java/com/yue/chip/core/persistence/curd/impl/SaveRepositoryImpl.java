package com.yue.chip.core.persistence.curd.impl;

import com.yue.chip.core.persistence.curd.RepositoryParameter;
import com.yue.chip.core.persistence.curd.SaveRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class SaveRepositoryImpl<T>  implements SaveRepository<T> {

	private EntityManager entityManager;
	
	public SaveRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int save(String jpql,Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int save(String jpql) {
		return save(jpql, null);
	}

}
