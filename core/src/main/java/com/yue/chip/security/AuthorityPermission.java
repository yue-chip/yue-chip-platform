package com.yue.chip.security;

/**
 * @author Mr.Liu
 * @date 2023/6/7 下午3:28
 */
public interface AuthorityPermission {
    public boolean hasPermission(String... permissions);
}
