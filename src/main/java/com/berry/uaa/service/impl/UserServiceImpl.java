package com.berry.uaa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.berry.uaa.common.ResultCode;
import com.berry.uaa.common.exceptions.BaseException;
import com.berry.uaa.dao.entity.User;
import com.berry.uaa.dao.service.IUserDaoService;
import com.berry.uaa.module.vo.LoginSuccessVo;
import com.berry.uaa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/12 10:49
 * fileName：UserServiceImpl
 * Use：
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserDaoService userDaoService;

    @Override
    public ResponseEntity<LoginSuccessVo> login(String username, String password, Boolean rememberMe) {
        // 用户是否存在
        User user = userDaoService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new BaseException(ResultCode.ACCOUNT_NOT_EXIST);
        }
        // 密码是否正确
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("expires", String.valueOf(300));
            // todo 获取 token
            String token = "";
            return new ResponseEntity<>(new LoginSuccessVo(token, 300, new LoginSuccessVo.UserInfo(user.getUsername(), user.getNickName())), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof DisabledException) {
                throw new BaseException(ResultCode.ACCOUNT_DISABLE);
            } else if (e instanceof LockedException) {
                throw new BaseException(ResultCode.ACCOUNT_LOCKED);
            } else if (e instanceof BadCredentialsException) {
                throw new BaseException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
            }
            throw new BaseException("unknow", e.getMessage());
        }
    }
}
