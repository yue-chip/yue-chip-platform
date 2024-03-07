package com.yue.chip.core;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @description: 分页查询
 * @author: Mr.Liu
 * @create: 2020-01-13 16:18
 */
public class YueChipPage extends PageRequest {

    private static final long serialVersionUID = -4541509938956089563L;

    public YueChipPage() {
        this(0,30, Sort.unsorted());
    }
    public YueChipPage(int page, int size, Sort sort){
        super(page,size,sort);
    }
    public YueChipPage(int page, int size){
        this(page,size,Sort.unsorted());
    }

}
