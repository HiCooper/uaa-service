package com.berry.uaa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.berry.uaa.common.ResultCode;
import com.berry.uaa.common.exceptions.BaseException;
import com.berry.uaa.dao.entity.User;
import com.berry.uaa.dao.service.IUserDaoService;
import com.berry.uaa.dao.service.IUserRoleDaoService;
import com.berry.uaa.module.mo.LoginMo;
import com.berry.uaa.module.mo.UserRegisterMo;
import com.berry.uaa.module.vo.LoginSuccessVo;
import com.berry.uaa.security.filter.AuthFilter;
import com.berry.uaa.security.filter.TokenProvider;
import com.berry.uaa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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

    @Autowired
    private IUserRoleDaoService userRoleDaoService;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public ResponseEntity<LoginSuccessVo> login(LoginMo loginMo, HttpServletResponse response) {
        // 用户是否存在
        User user = userDaoService.getOne(new QueryWrapper<User>().eq("username", loginMo.getUsername()));
        if (user == null) {
            throw new BaseException(ResultCode.ACCOUNT_NOT_EXIST);
        }
        // 密码是否正确
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginMo.getUsername(), loginMo.getPassword());
        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = this.tokenProvider.createAndSignToken(authentication, user.getId(), loginMo.isRememberMe());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(AuthFilter.AUTHORIZATION_HEADER, jwt);
            long expires = TokenProvider.TOKEN_VALIDITY_IN_MILLISECONDS / 1000;
            if (loginMo.isRememberMe()) {
                expires = TokenProvider.TOKEN_VALIDITY_IN_MILLISECONDS_FOR_REMEMBER_ME / 1000;
            }
            Cookie cookie = new Cookie(AuthFilter.AUTHORIZATION_HEADER, jwt);
            cookie.setMaxAge(Integer.parseInt(String.valueOf(expires)));
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            httpHeaders.add("expires", String.valueOf(expires));
            return new ResponseEntity<>(new LoginSuccessVo(jwt, expires, new LoginSuccessVo.UserInfo(user.getUsername(), user.getNickName())), httpHeaders, HttpStatus.OK);
        } catch (DisabledException | LockedException e) {
            throw new BaseException(ResultCode.ACCOUNT_DISABLE);
        } catch (BadCredentialsException e) {
            throw new BaseException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        } catch (Exception e) {
            throw new BaseException(ResultCode.FAIL);
        }
    }

    @Override
    public void register(UserRegisterMo mo) {
        // 1. 用户名是否存在
        User user = userDaoService.findOneByUsername(mo.getUsername()).orElse(null);
        if (user != null) {
            throw new BaseException(ResultCode.USERNAME_EXIST);
        }
        // 默认注册 未激活状态，需要管理员审核，并设置角色
        user = new User()
                .setActivated(false)
                .setCreateTime(new Date())
                .setUsername(mo.getUsername())
                .setNickName(mo.getNickName())
                .setEmail(mo.getEmail())
                .setPassword(new BCryptPasswordEncoder().encode(mo.getPassword()));
        userDaoService.save(user);
    }
}
