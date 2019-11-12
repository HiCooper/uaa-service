package com.berry.uaa.api;

import com.berry.uaa.common.Result;
import com.berry.uaa.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
