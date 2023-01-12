package com.yue.chip.core;

import com.yue.chip.core.persistence.entity.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Mr.Liu
 * @date 2023/1/10 下午3:03
 * @description BaseDefinition
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper=true)
public abstract class BaseDefinition extends BaseEntity {
}
