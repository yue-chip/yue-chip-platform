package com.yue.chip.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/8 下午2:51
 */
public interface SystemLogService {

    public void save(String actionName);

    public Page<? extends Object > list(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
