package com.yue.chip.authorization.service.impl;

import com.yue.chip.authorization.dao.RegisteredClientDao;
import com.yue.chip.authorization.dto.RegisteredClientAddDTO;
import com.yue.chip.authorization.service.RegisteredClientService;
import org.springframework.data.annotation.Reference;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 下午2:24
 */
@Service
public class RegisteredClientServiceImpl implements RegisteredClientService {

    @Reference
    private RegisteredClientDao registeredClientDao;

    @Override
    public void add(RegisteredClientAddDTO registeredClientAddDTO) {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(registeredClientAddDTO.getClientId())
                .clientIdIssuedAt(Instant.now())
                .clientName(registeredClientAddDTO.getClientName())
                .clientSecretExpiresAt(registeredClientAddDTO.getClientSecretExpiresAt().toInstant(ZoneOffset.UTC))
                .clientSecret("{noop}".concat(registeredClientAddDTO.getClientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("deviceDataProperty")
                .scope("deviceCommand")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(24)).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientDao.save(client);
    }
}
