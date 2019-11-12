package com.berry.uaa.api;

import com.berry.uaa.common.Result;
import com.berry.uaa.common.ResultFactory;
import com.berry.uaa.module.vo.LoginSuccessVo;
import com.berry.uaa.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IUserService userService;

    @ApiOperation("用户登录")
    @PostMapping(value = "/login")
    public ResponseEntity<LoginSuccessVo> login(@RequestParam String username,
                                                @RequestParam String password,
                                                @RequestParam(defaultValue = "false") Boolean rememberMe) {
        return userService.login(username, password, rememberMe);
    }

    @ApiOperation("用户详情")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @GetMapping(value = "/detail")
    public Result detail() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("用户信息更新")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @PostMapping(value = "/update")
    public Result update() {
        return ResultFactory.wrapper();
    }
}
