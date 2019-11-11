package com.berry.authserver.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/10/29 14:10
 * fileName：UserController
 * Use：
 */
@RestController
@RequestMapping("/api/resources")
public class UserController {

    /**
     * 需要用户角色权限
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "user")
    public String helloUser() {
        return "hello user";
    }

    /**
     * 需要管理角色权限
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin")
    public String helloAdmin() {
        return "hello admin";
    }

    /**
     * 需要客户端权限
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(value = "client")
    public String helloClient() {
        return "hello user authenticated by normal client";
    }

    /**
     * 需要受信任的客户端权限
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_TRUSTED_CLIENT')")
    @GetMapping(value = "trusted_client")
    public String helloTrustedClient() {
        return "hello user authenticated by trusted client";
    }

    @GetMapping(value = "principal")
    public Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping(value = "roles")
    public Object getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
