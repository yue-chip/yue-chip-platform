package com.yue.chip.core.persistence.curd;

import com.yue.chip.core.persistence.entity.BaseEntity;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
@NoRepositoryBean
public interface BaseDao<T extends BaseEntity> extends UpdateRepository<T>, SelectRepository<T>, SaveRepository<T>, DeleteRepository<T>, JpaRepositoryImplementation<T, Serializable> {

    /**
     *
     * @return
     */
    public Session getSession();

}
