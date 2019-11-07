package com.berry.uaaserver.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.berry.uaaserver.dao.entity.Role;
import com.berry.uaaserver.dao.mapper.RoleMapper;
import com.berry.uaaserver.dao.service.IRoleDaoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author HiCooper
 * @since 2018-12-02
 */
@Service
public class RoleDaoServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleDaoService {
}
