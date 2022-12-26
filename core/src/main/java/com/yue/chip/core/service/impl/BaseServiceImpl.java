package com.yue.chip.core.service.impl;

import com.yue.chip.core.Optional;
import com.yue.chip.core.persistence.curd.BaseDao;
import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @param <T>
 * @author mrliu
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {


    @Autowired
    private BaseDao<T> baseDao;

//    @Autowired
//    JPAQueryFactory queryFactory;

    @Override
    public List<T> findAll() {
        return baseDao.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseDao.findAll(sort);
    }

    @Override
    public List<T> findAllById(Iterable<Serializable> ids) {
        return baseDao.findAllById(ids);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return baseDao.saveAll(entities);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return baseDao.saveAndFlush(entity);
    }

    @Override
    public void update(T entity) {
        baseDao.update(entity);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        baseDao.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch() {
        baseDao.deleteAllInBatch();
    }

    @Override
    public T getOne(Serializable id) {
        return baseDao.getOne(id);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return baseDao.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return baseDao.findAll(example, sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Page<T> page = baseDao.findAll(pageable);
        return page;
    }

    @Override
    public <S extends T> S save(S entity) {
        if (Objects.nonNull(entity)){
            if (Objects.nonNull( ((BaseEntity)entity).getId())){
                this.update(entity);
                return entity;
            }
            return baseDao.save(entity);
        }
        return entity;
    }

    @Override
    public Optional<T> findById(Serializable id) {
        try {
            if (Objects.nonNull(id)) {
                java.util.Optional<T> optional = baseDao.findById(id);
                if (optional.isPresent()) {
                    return Optional.of(optional.get());
                }else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Serializable id) {
        return baseDao.existsById(id);
    }

    @Override
    public long count() {
        return baseDao.count();
    }

    @Override
    @Transactional
    public void deleteById(Serializable id) {
        baseDao.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        baseDao.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        baseDao.deleteAll();
    }

    @Override
    public <S extends T> java.util.Optional<S> findOne(Example<S> example) {
        return baseDao.findOne(example);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        Page<S> page = (Page<S>) baseDao.findAll((Example<T>) example,pageable);
        return page;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return baseDao.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return baseDao.exists(example);
    }

    @Override
    public List<?> findAll(String jpql) {
        return baseDao.findAll(jpql);
    }

    @Override
    public Object findOne(String jpql) {
        return baseDao.findOne(jpql);
    }

    @Override
    public List<?> findAll(String jpql, Map<String, Object> searchParameter) {
        return baseDao.findAll(jpql, searchParameter);
    }

    @Override
    public Page<?> findNavigator(Pageable pageable, String jpql) {
        return baseDao.findNavigator(pageable,jpql);
    }

    @Override
    public Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> searchParameter) {
        return baseDao.findNavigator(pageable, jpql, searchParameter);
    }

    @Override
    public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql) {
        return baseDao.findNavigatorByNativeSql(pageable, sql);
    }

    @Override
    public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter, Class<?> returnType) {
        return baseDao.findNavigatorByNativeSql(pageable, sql, searchParameter, returnType);
    }

//    @Override
//    public List<T> find(JPAQuery<T> jpaQuery) {
//        return jpaQuery.fetch();
//    }
}
