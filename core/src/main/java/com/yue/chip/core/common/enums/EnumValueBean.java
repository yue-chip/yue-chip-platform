package com.yue.chip.core.common.enums;

import lombok.Builder;
import lombok.Data;

/**
 * @author Mr.Liu
 * @date 2023/7/10 上午10:54
 */
@Data
@Builder
public class EnumValueBean {

    private Integer key;

    private String desc;

    private String name;

}
