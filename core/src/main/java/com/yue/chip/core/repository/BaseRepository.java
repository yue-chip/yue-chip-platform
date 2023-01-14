package com.yue.chip.core.repository;

import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.core.service.BaseService;

/**
 * @author Mr.Liu
 * @date 2023/1/14 上午10:50
 * @description BaseRepository
 */
public interface BaseRepository<T extends BaseEntity> extends BaseService<T> {
}
