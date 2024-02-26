package com.yue.chip.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-18 23:01
 **/
@JsonIgnoreProperties(
        ignoreUnknown = true,
        value = {"content", "pageable", "sort", "numberOfElements", "empty", "number", "size"}
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface IPageResultData<T> extends IResultData<T> , Page<T> {

    int getTotalPages();

    long getTotalElements();

    public boolean isLast();

    public boolean isFirst();

    public Integer getPageNumber();

    public Integer getPageSize();
}
