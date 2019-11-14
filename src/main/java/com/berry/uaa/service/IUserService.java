package com.berry.uaa.service;

import com.berry.uaa.module.UserInfoVo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/14 15:46
 * fileName：IUserService
 * Use：
 */
public interface IUserService {
    UserInfoVo detail(String username);
}
