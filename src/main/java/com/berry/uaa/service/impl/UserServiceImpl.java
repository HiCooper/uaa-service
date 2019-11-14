package com.berry.uaa.service.impl;

import com.berry.uaa.dao.entity.User;
import com.berry.uaa.dao.service.IUserDaoService;
import com.berry.uaa.module.UserInfoVo;
import com.berry.uaa.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/14 15:46
 * fileName：UserServiceImpl
 * Use：
 */
@Service
public class UserServiceImpl implements IUserService {

    private final IUserDaoService userDaoService;

    public UserServiceImpl(IUserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public UserInfoVo detail(String username) {
        User user = userDaoService.findOneByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
