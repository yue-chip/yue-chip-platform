package com.yue.chip.core.persistence.curd.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yue.chip.core.persistence.curd.RepositoryParameter;
import com.yue.chip.core.persistence.curd.UpdateRepository;
import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class UpdateRepositoryImpl<T>  implements UpdateRepository<T> {

	private EntityManager entityManager;
	
	private JpaEntityInformation<T, Serializable> entityInformation;
	
	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public UpdateRepositoryImpl(EntityManager entityManager, JpaEntityInformation<T, Serializable> entityInformation, SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int update(String jpql) {
		return update(jpql, null);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public int update(String jpql,Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	public void update(T entity) {
		BaseEntity newEntity = (BaseEntity)entity;
//		if (Objects.isNull(newEntity.getVersion())){
//			BusinessException.throwException("该数据版本号不能为空");
//		}
		Serializable id = entityInformation.getId(entity);
		Optional<T> optional = simpleJpaRepository.findById(id);
		if (optional.isPresent()) {
			BaseEntity oldEntity = (BaseEntity) optional.get();
			BeanUtil.copyProperties(newEntity, oldEntity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			simpleJpaRepository.save((T) oldEntity);
		}

	}

}
