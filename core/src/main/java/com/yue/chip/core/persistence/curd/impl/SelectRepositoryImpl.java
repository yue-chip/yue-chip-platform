package com.yue.chip.core.persistence.curd.impl;

import com.yue.chip.core.IEnum;
import com.yue.chip.core.persistence.curd.RepositoryParameter;
import com.yue.chip.core.persistence.curd.SelectRepository;
import com.yue.chip.utils.SqlUtil;
import com.yue.chip.utils.TenantSqlUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.sf.jsqlparser.JSQLParserException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import springfox.documentation.service.ParameterSpecification;

import java.io.Serializable;
import java.util.*;

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
//		Session session = entityManager.unwrap(Session.class);
//		SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor)session.getSessionFactory();
//		Map replacements = Objects.isNull(searchParameter)?Collections.EMPTY_MAP:searchParameter;
//		QueryTranslatorImpl queryTranslator=new QueryTranslatorImpl(jpql,jpql,replacements,sessionFactoryImplementor);
//		queryTranslator.compile(replacements,true);
//		String sql = queryTranslator.getSQLString();
//		List<ParameterSpecification> parameter = queryTranslator.getCollectedParameterSpecifications();
//		for(ParameterSpecification parameterSpecification : parameter){
//			StringBuilder sb = new StringBuilder();
//			int index = sql.indexOf("?");
//			sb.append(sql.substring(0,index));
//			sb.append(":");
//			sb.append(((NamedParameterSpecification)parameterSpecification).getName());
//			sb.append(sql.substring(index+1));
//			sql = sb.toString();
//		}
//		Set<String> keys = searchParameter.keySet();
//		keys.forEach(key->{
//			Object o = searchParameter.get(key);
//			if (o instanceof IEnum){
//				searchParameter.put(key,((IEnum)o).getKey());
//			}
//		});
//		return getCountByNativeSql(sql,searchParameter);
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
//		String sql = query.createQuery().unwrap(org.eclipse.persistence.jpa.JpaQuery.class).getDatabaseQuery().getSQLString();
		String sql = query.unwrap(org.hibernate.query.Query.class).getQueryString();
		return getCountByNativeSql(sql,null);

	}

	private Long getCountByNativeSql(String sql,Map<String, Object> searchParameter) {
		StringBuffer countSql = new StringBuffer();
		try {
			sql = TenantSqlUtil.sqlReplace(sql);
		} catch (JSQLParserException e) {
			throw new RuntimeException(e);
		}
		try {
			sql = SqlUtil.sqlReplaceOrderBy(sql);
		} catch (JSQLParserException e) {
			throw new RuntimeException(e);
		}
		countSql.append(" select count(1) from (");
		countSql.append(sql);
		countSql.append(") tb");
		Query query = entityManager.createNativeQuery(countSql.toString());
		query = RepositoryParameter.setParameter(query, searchParameter);
		return Long.valueOf(query.getSingleResult().toString());
	}

}
