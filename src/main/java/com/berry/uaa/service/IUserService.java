package com.berry.uaa.service;

import com.berry.uaa.module.vo.LoginSuccessVo;
import org.springframework.http.ResponseEntity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/12 10:49
 * fileName：IUserService
 * Use：
 */
public interface IUserService {
    /**
     * 用户登录
     *
     * @param username   用户名
     * @param password   密码
     * @param rememberMe 是否记住
     * @return
     */
    ResponseEntity<LoginSuccessVo> login(String username, String password, Boolean rememberMe);
}
