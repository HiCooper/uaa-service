package com.berry.uaa.service;

import com.berry.uaa.module.mo.LoginMo;
import com.berry.uaa.module.mo.UserRegisterMo;
import com.berry.uaa.module.vo.LoginSuccessVo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

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
     * @param loginMo
     * @param response
     * @return
     */
    ResponseEntity<LoginSuccessVo> login(LoginMo loginMo, HttpServletResponse response);

    /**
     * 用户注册
     *
     * @param mo 请求参数
     */
    void register(UserRegisterMo mo);
}
