package com.yue.chip.core.service.impl;

import com.yue.chip.core.SystemLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/8 下午4:16
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {
    @Override
    public void save(String actionName) {

    }

    @Override
    public Page<? extends Object> list(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        return null;
    }
}
