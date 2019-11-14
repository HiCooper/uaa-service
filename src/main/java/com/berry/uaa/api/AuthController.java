package com.berry.uaa.api;

import com.berry.uaa.common.Result;
import com.berry.uaa.common.ResultFactory;
import com.berry.uaa.module.mo.LoginMo;
import com.berry.uaa.module.mo.UserRegisterMo;
import com.berry.uaa.module.vo.LoginSuccessVo;
import com.berry.uaa.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Berry_Cooper.
 * Description: 用户登录，注册，权限开放
 * Date: 2018/05/03
 * fileName MultyTestController
 */
@RestController
@RequestMapping("/auth")
@Api(value = "授权", tags = "授权")
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public ResponseEntity<LoginSuccessVo> login(@Valid @RequestBody LoginMo loginMo, HttpServletResponse response) {
        return userService.login(loginMo, response);
    }

    @ApiOperation("注册-需审核")
    @PostMapping("register")
    public Result register(@Validated @RequestBody UserRegisterMo mo) {
        userService.register(mo);
        return ResultFactory.wrapper();
    }
}
