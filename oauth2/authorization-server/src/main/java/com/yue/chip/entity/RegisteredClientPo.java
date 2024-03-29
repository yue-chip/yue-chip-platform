package com.yue.chip.entity;

import com.yue.chip.core.persistence.JpaInterceptor;
import com.yue.chip.core.persistence.entity.IBaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author coby
 * @description: TODO
 * @date 2024/2/5 上午11:19
 */
@Entity
@Table(name = "oauth2_registered_client",indexes = {@Index(columnList = "clientId")})
@SuperBuilder
@EntityListeners({AuditingEntityListener.class, JpaInterceptor.class})
@NoArgsConstructor
@Comment("oauth2授权信息")
public class RegisteredClientPo implements IBaseEntity {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    private String clientId;

    private LocalDateTime clientIdIssuedAt;

    private String clientSecret;

    private LocalDateTime clientSecretExpiresAt;

    private String clientName;

    @Column(length = 1000)
    private String clientAuthenticationMethods;

    @Column(length = 1000)
    private String authorizationGrantTypes;

    @Column(length = 1000)
    private String redirectUris;

    @Column(length = 1000)
    private String scopes;

    @Column(length = 1000)
    private String clientSettings;

    @Column(length = 2000)
    private String tokenSettings;

    @Column(length = 2000)
    private String postLogoutRedirectUris;

    private Long tenantNumber;
}
