package com.yue.chip.authorization.dao.impl;

import com.yue.chip.authorization.dao.RegisteredClientDaoEx;
import org.springframework.data.annotation.Reference;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:19
 */
public class RegisteredClientDaoImpl implements RegisteredClientDaoEx {

    @Reference
    private RegisteredClientRepository registeredClientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        registeredClientRepository.save(registeredClient);
    }
}
