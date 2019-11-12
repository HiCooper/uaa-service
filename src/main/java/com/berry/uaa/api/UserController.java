package com.berry.uaa.api;

import com.berry.uaa.common.Result;
import com.berry.uaa.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/10/29 14:10
 * fileName：UserController
 * Use：
 */
@RestController
@RequestMapping("/api")
@Api(tags = "用户授权访问API")
public class UserController {

    @ApiOperation("用户登录")
    @GetMapping(value = "/login")
    public Result login() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("用户详情")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @GetMapping(value = "/detail")
    public Result detail() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("用户信息更新")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @GetMapping(value = "/update")
    public Result update() {
        return ResultFactory.wrapper();
    }


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
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @GetMapping("admin")
    public String helloAdmin(HttpServletRequest request) {
        request.getAuthType();
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
