package com.yue.chip.dao;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:19
 */
public interface RegisteredClientDaoEx {

    /**
     * 保存oauth2鉴权信息
     * @param registeredClient
     */
    public void save(@NotNull RegisteredClient registeredClient);
}
