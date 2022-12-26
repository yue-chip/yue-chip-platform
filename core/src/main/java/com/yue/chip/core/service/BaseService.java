package com.yue.chip.core.service;

import com.yue.chip.core.Optional;
import com.yue.chip.core.persistence.entity.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mrliu
 *
 * @param <T>
 */
public interface BaseService<T extends BaseEntity>{

	public List<T> findAll();

	public List<T> findAll(Sort sort);

	public List<T> findAllById(Iterable<Serializable> ids);

	public <S extends T> List<S> saveAll(Iterable<S> entities);

	public <S extends T> S saveAndFlush(S entity);
	
	public void update(T entity);

	public void deleteInBatch(Iterable<T> entities);

	public void deleteAllInBatch();

	public T getOne(Serializable id);

	public <S extends T> List<S> findAll(Example<S> example);

	public <S extends T> List<S> findAll(Example<S> example, Sort sort);

	public Page<T> findAll(Pageable pageable);

	public <S extends T> S save(S entity);

	public Optional<T> findById(Serializable id);

	public boolean existsById(Serializable id);

	public long count();
	
	public void deleteById(Serializable id);

	public void delete(T entity);

	public void deleteAll(Iterable<? extends T> entities);

	public void deleteAll();

	public <S extends T> java.util.Optional<S> findOne(Example<S> example);

	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

	/**
	 * 使用jpql语句查询数据
	 *
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	List<?> findAll(String jpql);

	/**
	 * 使用jpql语句查询数据返回单个对象
	 *
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	Object findOne(String jpql);

	/**
	 * 使用jpql语句查询
	 *
	 * @param jpql
	 *            jpql语句
	 * @return T
	 */
	List<?> findAll(String jpql, Map<String, Object> searchParameter);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param pageable
	 * @param jpql
	 * @return
	 */
	Page<?> findNavigator(Pageable pageable, String jpql);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param jpql
	 * @param pageable
	 * @param searchParameter
	 * @return
	 */
	Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> searchParameter);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param pageable
	 * @param sql
	 * @return
	 */
	Page<?> findNavigatorByNativeSql(Pageable pageable, String sql);

	/**
	 *
	 * @param pageable
	 * @param sql
	 * @param searchParameter
	 * @param returnType
	 * @return
	 */
	Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter,Class<?> returnType);


	public <S extends T> long count(Example<S> example);

	public <S extends T> boolean exists(Example<S> example);

	/**
	 * 查询
	 * @param jpaQuery
	 * @return
	 */
//	List<T> find(JPAQuery<T> jpaQuery);


}
