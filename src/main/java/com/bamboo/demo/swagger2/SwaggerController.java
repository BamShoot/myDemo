package com.bamboo.demo.swagger2;

import com.bamboo.demo.entity.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "user")
public class SwaggerController {

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public String login(@ApiParam(name = "username", value = "用户名", required = true) @RequestParam String username,
                        @ApiParam(name = "password", value = "密码", required = true) @RequestParam String password) {
        return "{'username':'" + username + "', 'password':'" + password + "'}";
    }


    @ApiOperation("新增用户接口")
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        return user.toString();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(dataTypeClass = String.class, paramType = "header", name = "phone", required = true, value = "手机号"),
            @ApiImplicitParam(dataTypeClass = String.class, paramType = "query", name = "nickname", required = true, value = "nickname", defaultValue = "双击666"),
            @ApiImplicitParam(dataTypeClass = String.class, paramType = "path", name = "platform", required = true, value = "平台", defaultValue = "PC"),
            @ApiImplicitParam(dataTypeClass = String.class, paramType = "body", name = "password", required = true, value = "密码")
    })
    @PutMapping(value = "/{platform}/regist")
    public String regist(@RequestHeader String phone, @RequestParam String nickname, @PathVariable String platform, @RequestBody String password){
        return "{'username':'" + phone + "', 'nickname':'" + nickname + "', 'platform': '" + platform + "', 'password':'"+password+"'}";
    }

}
