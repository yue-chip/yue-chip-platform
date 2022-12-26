package com.yue.chip.core.persistence.curd.impl;

import com.yue.chip.core.persistence.curd.DeleteRepository;
import com.yue.chip.core.persistence.curd.RepositoryParameter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class DeleteRepositoryImpl<T>  implements DeleteRepository<T> {

	private EntityManager entityManager;
	
	public DeleteRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	public int delete(String jpql) {
		return delete(jpql, null);
	}

	@Override
	public int delete(String jpql, Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}
	
	

}
