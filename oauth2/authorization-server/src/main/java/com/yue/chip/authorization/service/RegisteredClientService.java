package com.yue.chip.authorization.service;

import com.yue.chip.authorization.dto.RegisteredClientAddDTO;
import jakarta.validation.constraints.NotNull;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:24
 */
public interface RegisteredClientService {

    /**
     * 新建oauth2鉴权信息
     * @param registeredClientAddDTO
     */
    public void add(@NotNull RegisteredClientAddDTO registeredClientAddDTO);
}
