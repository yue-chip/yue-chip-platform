package com.yue.chip.core.common.enums;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Mr.Liu
 * @date 2023/7/6 上午11:04
 */
@Data
@Builder
public class EnumPersistenceBean {

    private String code;

    private String version;

    private String value;
}
