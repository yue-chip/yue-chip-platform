package com.yue.chip.core.repository.impl;

import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.core.repository.BaseRepository;
import com.yue.chip.core.service.impl.BaseServiceImpl;

/**
 * @author Mr.Liu
 * @date 2023/1/14 上午10:51
 * @description BaseRepositoryImpl
 */
public class BaseRepositoryImpl<T extends BaseEntity> extends BaseServiceImpl<T> implements BaseRepository<T> {
}
