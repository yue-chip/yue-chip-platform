package com.yue.chip.core.persistence.curd.impl;

import com.yue.chip.core.persistence.curd.RepositoryParameter;
import com.yue.chip.core.persistence.curd.SelectRepository;
import com.yue.chip.utils.SqlOrderOptimizeUtil;
import com.yue.chip.utils.TenantSqlUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * 
 * @author mrliu
 *
 * @param <T>
 */
public class SelectRepositoryImpl<T> implements SelectRepository<T> {

	private final Integer maxResults = 50000;

	private EntityManager entityManager;

	private Class<T> clazz;

	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public SelectRepositoryImpl(EntityManager entityManager, Class<T> clazz,
                                SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	@Override
	public List<?> findAll(String jpql) {
		return findAll(jpql, null);
	}

	@Override
	public Object findOne(String jpql) {
		Pageable pageable = PageRequest.of(1,1);
		Page<?> page = findNavigator(pageable,jpql);
		List<?> list = page.getContent();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<?> findAll(String jpql,Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		query.setMaxResults(maxResults);
		query = RepositoryParameter.setParameter(query, searchParameter);
		return query.getResultList();
	}

	@Override
	public Page<?> findNavigator(Pageable pageable, String jpql) {
		return findNavigator(pageable, jpql, null);
	}

	@Override
	public Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> searchParameter) {
		searchParameter = (Objects.isNull(searchParameter) || searchParameter.isEmpty())?new HashMap<>():searchParameter;
		List<?> list = (List<?>) executeJpql(pageable, jpql, searchParameter);
		Long total = getCount(jpql, searchParameter);
		Page page = new PageImpl(list,pageable,total);
		return page;
	}

	@Override
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql) {
		return findNavigatorByNativeSql(pageable,sql,null,null);
	}

	@Override
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter, Class<?> returnType) {
		searchParameter = (Objects.isNull(searchParameter) || searchParameter.isEmpty())?new HashMap<>():searchParameter;
		List<?> list = (List<?>) executeSql(pageable, sql, searchParameter,returnType);
		Long total = getCountByNativeSql(sql, searchParameter);
		Page page = new PageImpl(list,pageable,total);
		return page;
	}

	/**
	 * 分页查询
	 *
	 * @param pageable
	 * @param jpql
	 * @param searchParameter
	 * @return
	 */
	private List<?> executeJpql(Pageable pageable, String jpql, Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, searchParameter);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	private List<?> executeSql(Pageable lionPage, String sql, Map<String, Object> searchParameter,Class<?> returnType) {
		Query query = null;
		if (Objects.nonNull(returnType) && !Objects.equals(returnType,Map.class) && !Objects.equals(returnType,HashMap.class)) {
			query = entityManager.createNativeQuery(sql, returnType);
		}else if (Objects.isNull(returnType) || Objects.equals(returnType,Map.class) || Objects.equals(returnType,HashMap.class)){
			query = entityManager.createNativeQuery(sql);
			query = query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}
		query = RepositoryParameter.setParameter(query, searchParameter);
		query.setFirstResult(lionPage.getPageNumber() * lionPage.getPageSize());
		query.setMaxResults(lionPage.getPageSize());
		return query.getResultList();
	}

	/**
	 * 查询总行数
	 *
	 * @param jpql
	 * @param searchParameter
	 * @return
	 */
	private Long getCount(String jpql, Map<String, Object> searchParameter) {
		String jpqlTmp = jpql;
		jpql = jpql.toLowerCase();
		if (jpql.indexOf("from")>-1){
			jpql = " select count(*) " + jpqlTmp.substring(jpql.indexOf("from"));
		}
//		jpql = SqlOrderOptimizeUtil.sqlReplace(jpql);
		return getCountByJql(jpql,searchParameter);
	}

	private Long getCountByNativeSql(String sql,Map<String, Object> searchParameter) {
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(1) from (");
//		countSql.append(replace(sql));
		countSql.append(sql);
		countSql.append(") tb");
		Query query = entityManager.createNativeQuery(countSql.toString());
		return getCount(query,searchParameter);
	}

	private Long getCountByJql(String jpql,Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		return getCount(query,searchParameter);
	}

	private String replace(String sqlOrJpql) {
//		sqlOrJpql = SqlWhereOptimizeUtil.sqlReplace(sqlOrJpql);
		sqlOrJpql = SqlOrderOptimizeUtil.sqlReplace(sqlOrJpql);
		sqlOrJpql = TenantSqlUtil.sqlReplace(sqlOrJpql);
		return sqlOrJpql;
	}

	private Long getCount(Query query,Map<String, Object> searchParameter) {
		query = RepositoryParameter.setParameter(query, searchParameter);
		return Long.valueOf(query.getSingleResult().toString());
	}

}
