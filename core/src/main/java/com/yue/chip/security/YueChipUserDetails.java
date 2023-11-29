package com.yue.chip.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @description: 此类只为解决dubbo序列化问题
 * @author: Mr.Liu
 * @create: 2020-01-24 18:55
 */
public class YueChipUserDetails extends User implements Serializable {

    private static final long serialVersionUID = -883666601L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long tenantNumber;

    public YueChipUserDetails() {
        this(null,UUID.randomUUID().toString(),UUID.randomUUID().toString(), null,getGrantedAuthority());
    }

    public YueChipUserDetails(Long id, String username, String password, Long tenantNumber, Collection<? extends GrantedAuthority> authorities) {
        super(StringUtils.hasText(username)?username:UUID.randomUUID().toString(), StringUtils.hasText(password)?password:UUID.randomUUID().toString(), Objects.isNull(authorities)?getGrantedAuthority():authorities);
        this.id = id;
        this.tenantNumber = tenantNumber;
    }

    public YueChipUserDetails(Long id, String username, String password, Long tenantNumber, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.tenantNumber = tenantNumber;
    }

    private static List<GrantedAuthority> getGrantedAuthority(){
        YueChipSimpleGrantedAuthority grantedAuthority = new YueChipSimpleGrantedAuthority();
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(grantedAuthority);
        return list;
    }
}
