package com.berry.uaa.api.oauth2;

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
 * @date 2019/10/29 14:10
 * fileName：UserController
 * Use：受oauth2 认证保护的用户资源，需要 oauth2 认证生成的 access_token 方可访问
 */
@RestController
@RequestMapping("/api/resources")
@Api(tags = "用户授权访问API")
public class UserResourceController {

    @ApiOperation("用户详情")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @GetMapping(value = "/detail")
    public Result detail() {
        return ResultFactory.wrapper();
    }

    @ApiOperation("用户信息更新")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_INNER_SERVICE')")
    @PostMapping(value = "/update")
    public Result update() {
        return ResultFactory.wrapper();
    }
}
