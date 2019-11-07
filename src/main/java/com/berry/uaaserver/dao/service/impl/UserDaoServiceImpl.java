package com.berry.uaaserver.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.berry.uaaserver.dao.entity.Role;
import com.berry.uaaserver.dao.entity.User;
import com.berry.uaaserver.dao.mapper.UserMapper;
import com.berry.uaaserver.dao.service.IUserDaoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author HiCooper
 * @since 2018-12-02
 */
@Service
public class UserDaoServiceImpl extends ServiceImpl<UserMapper, User> implements IUserDaoService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Optional<User> findOneByUsername(String lowercaseLogin) {
        return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().eq("username", lowercaseLogin)));
    }

    @Override
    public Set<Role> findRoleListByUserId(Integer userId) {
        return userMapper.getRolesByUserId(userId);
    }
}
