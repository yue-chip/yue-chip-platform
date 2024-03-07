package com.yue.chip.core;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * @author coby
 * @description: TODO
 * @date 2024/3/7 下午6:14
 */
public class YueChipPageSerializable<T> extends PageImpl<T> implements PageSerializable<T>{
    public YueChipPageSerializable(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public YueChipPageSerializable(List<T> content) {
        super(content);
    }

    public YueChipPageSerializable() {
        super(Collections.emptyList());
    }
}
