package com.yue.chip.core.persistence.curd;

import com.yue.chip.core.persistence.curd.impl.DeleteRepositoryImpl;
import com.yue.chip.core.persistence.curd.impl.SaveRepositoryImpl;
import com.yue.chip.core.persistence.curd.impl.SelectRepositoryImpl;
import com.yue.chip.core.persistence.curd.impl.UpdateRepositoryImpl;
import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.core.persistence.entity.IBaseEntity;
import com.yue.chip.utils.id.SnowflakeUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author mrliu
 *
 */
@NoRepositoryBean
public abstract class YueChipSimpleJpaRepository<T extends IBaseEntity> extends SimpleJpaRepository<T, Serializable> implements BaseDao<T> {

	private SelectRepository<T> selectRepository;

	private UpdateRepository<T> updateRepository;

	private SaveRepository<T> saveRepository;

	private DeleteRepository<T> deleteRepository;

	protected EntityManager entityManager;

	protected JpaEntityInformation<T, Serializable> entityInformation;

	// T的具体类
	protected Class<T> clazz;

	public YueChipSimpleJpaRepository(JpaEntityInformation<T, Serializable> entityInformation,
									  EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.clazz = entityInformation.getJavaType();
		updateRepository = new UpdateRepositoryImpl<T>(entityManager, entityInformation, this);
		selectRepository = new SelectRepositoryImpl<T>(entityManager, clazz, this);
		saveRepository = new SaveRepositoryImpl<T>(entityManager);
		deleteRepository = new DeleteRepositoryImpl<T>(entityManager);
	}


	@Override
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	@Transactional(readOnly=true)
	public List<?> findAll(String jpql) {
		return selectRepository.findAll(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Object findOne(String jpql) {
		return selectRepository.findOne(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public List<?> findAll(String jpql, Map<String, Object> parameter) {
		return selectRepository.findAll(jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigator(Pageable pageable, String jpql) {
		return selectRepository.findNavigator(pageable, jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> parameter) {
		return selectRepository.findNavigator(pageable, jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql) {
		return selectRepository.findNavigatorByNativeSql(pageable, sql);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter, Class<?> returnType) {
		return selectRepository.findNavigatorByNativeSql(pageable, sql, searchParameter, returnType);
	}

	@Override
	public Page<?> findNavigator(String jpql) {
		return selectRepository.findNavigator(jpql);
	}

	@Override
	public Page<?> findNavigator(String jpql, Map<String, Object> searchParameter) {
		return selectRepository.findNavigator(jpql, searchParameter);
	}

	@Override
	public Page<?> findNavigatorByNativeSql(String sql) {
		return selectRepository.findNavigatorByNativeSql(sql);
	}

	@Override
	public Page<?> findNavigatorByNativeSql(String sql, Map<String, Object> searchParameter, Class<?> returnType) {
		return selectRepository.findNavigatorByNativeSql(sql,searchParameter,returnType);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int update(String jpql) {
		return updateRepository.update(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int update(String jpql, Map<String, Object> parameter) {
		return updateRepository.update(jpql, parameter);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void update(T entity) {
		updateRepository.update(entity);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int save(String jpql) {
		return saveRepository.save(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int save(String jpql, Map<String, Object> parameter) {
		return saveRepository.save(jpql, parameter);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int delete(String jpql) {
		return deleteRepository.delete(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int delete(String jpql, Map<String, Object> parameter) {
		return deleteRepository.delete(jpql, parameter);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor = Throwable.class)
	public <S extends T> S save(S entity) {
		BaseEntity baseEntity = (BaseEntity)entity;
//		if (Objects.isNull(baseEntity.getId())){
//			baseEntity.setId(SnowflakeUtil.getId());
//		}
//		if (this.entityInformation.isNew(entity)) {
//			baseEntity.setVersion(0L);
//		}
		return super.save(entity);
	}
}
