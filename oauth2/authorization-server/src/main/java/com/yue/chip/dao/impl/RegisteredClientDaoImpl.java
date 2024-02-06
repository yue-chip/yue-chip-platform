package com.yue.chip.dao.impl;

import com.yue.chip.dao.RegisteredClientDaoEx;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:19
 */
public class RegisteredClientDaoImpl implements RegisteredClientDaoEx {

    @Resource
    private RegisteredClientRepository registeredClientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        registeredClientRepository.save(registeredClient);
    }
}
