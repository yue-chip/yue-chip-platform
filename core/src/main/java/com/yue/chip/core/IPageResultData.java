package com.yue.chip.core;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-18 23:01
 **/
public interface IPageResultData<T> extends IResultData<T> {

    int getTotalPages();

    long getTotalElements();

    public boolean isLast();

    public boolean isFirst();

    public Integer getPageNumber();

    public Integer getPageSize();
}
