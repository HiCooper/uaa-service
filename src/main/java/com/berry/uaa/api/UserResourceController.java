package com.berry.uaa.api;

import com.berry.uaa.module.UserInfoVo;
import com.berry.uaa.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/10/29 14:10
 * fileName：UserController
 * Use：受oauth2 认证保护的用户资源，需要 oauth2 认证生成的 access_token 方可访问
 */
@RestController
@RequestMapping("/api/resources")
public class UserResourceController {

    private final IUserService userService;

    public UserResourceController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户 oauth2 认证通过的 请求获取用户信息
     *
     * @param username 用户名
     * @return userInfo
     */
    @GetMapping(value = "/detail")
    public ResponseEntity<UserInfoVo> detail(@RequestParam String username) {
        UserInfoVo userInfoVo = userService.detail(username);
        return new ResponseEntity<>(userInfoVo, HttpStatus.OK);
    }
}
