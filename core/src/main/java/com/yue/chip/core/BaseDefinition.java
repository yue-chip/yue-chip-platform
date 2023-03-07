package com.yue.chip.core;

import com.yue.chip.core.persistence.curd.BaseDao;
import com.yue.chip.core.persistence.entity.BaseEntity;
import com.yue.chip.utils.SpringContextUtil;
import jakarta.persistence.MappedSuperclass;
import jakarta.transaction.Synchronization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @date 2023/1/10 下午3:03
 * @description BaseDefinition
 */
@Data
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDefinition extends BaseEntity {
}
