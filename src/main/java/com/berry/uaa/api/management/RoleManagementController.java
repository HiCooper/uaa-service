package com.berry.uaa.api.management;

import com.berry.uaa.common.Result;
import com.berry.uaa.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/12 9:10
 * fileName：RoleManagementController
 * Use：角色管理
 */
@RestController
@RequestMapping("/api/management/role")
@Api(tags = "角色管理")
public class RoleManagementController {

    @ApiOperation("获取角色列表")
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @PostMapping(value = "/list")
    public Result listRole() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("角色详情")
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @GetMapping(value = "/detail")
    public Result detailRole() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("创建角色")
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @PostMapping(value = "/create")
    public Result createRole() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("删除角色")
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @PostMapping(value = "/delete")
    public Result deleteRole() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("修改角色")
    @PreAuthorize("hasRole('ROLE_INNER_SERVICE')")
    @PostMapping(value = "/edit")
    public Result editRole() {
        return ResultFactory.wrapper();
    }
}
