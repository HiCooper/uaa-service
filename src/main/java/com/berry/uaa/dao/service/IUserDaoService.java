package com.berry.uaa.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.berry.uaa.dao.entity.Role;
import com.berry.uaa.dao.entity.User;

import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author HiCooper
 * @since 2018-12-02
 */
public interface IUserDaoService extends IService<User> {

    /**
     * 根据用户名获取用户
     *
     * @param lowercaseLogin lowercaseLogin
     * @return User
     */
    Optional<User> findOneByUsername(String lowercaseLogin);

    /**
     * 根据用户id获取角色列表
     *
     * @param userId userId
     * @return set Role
     */
    Set<Role> findRoleListByUserId(Integer userId);
}
