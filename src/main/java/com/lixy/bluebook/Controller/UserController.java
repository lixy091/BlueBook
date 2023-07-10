package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Service.UserService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Api(tags = "用户操作相关")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("获取用户基本信息")
    @GetMapping("/getUser")
    public ResponseData getUser(){
        return userService.getUser();
    }

}
