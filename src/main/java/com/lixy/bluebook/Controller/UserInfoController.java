package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.UserInfo;
import com.lixy.bluebook.Service.UserInfoService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@RestController
@Api(tags = "用户详细信息")
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("保存用户详情")
    @PostMapping("/save")
    public ResponseData saveUserInfo(
            @ApiParam(value = "" , name = "" , required = true)
            @RequestBody UserInfo userInfo
    ){
        return userInfoService.saveUserInfo(userInfo);
    }
}
