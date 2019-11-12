package com.berry.uaa.security;

import com.berry.uaa.common.exceptions.BaseException;
import com.berry.uaa.dao.entity.Role;
import com.berry.uaa.dao.entity.User;
import com.berry.uaa.dao.service.IUserDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Resource
    private IUserDaoService userDaoService;

    /**
     * 根据用户名获取用户并创建授权用户信息
     * success->创建用户
     * fail-> 抛出异常 Bad credentials
     *
     * @param username 用户名
     * @return 安全用户详情
     */
    @Override
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);
        String lowercaseLogin = username.toLowerCase(Locale.CHINA);
        Optional<User> oneByUsername = userDaoService.findOneByUsername(lowercaseLogin);
        return oneByUsername.map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    /**
     * 创建授权用户
     *
     * @param username 用户名
     * @param user     用户基本信息
     * @return 安全用户
     */
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String username, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + username + " was not activated");
        }
        Set<Role> roleList = userDaoService.findRoleListByUserId(user.getId());
        if (roleList == null) {
            throw new BaseException("403", "用户尚未分配角色");
        }
        List<GrantedAuthority> grantedAuthorities = roleList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.isEnabled(), user.getExpired() == null || user.getExpired().after(new Date()), true, !user.isLocked(),
                grantedAuthorities);
    }
}
