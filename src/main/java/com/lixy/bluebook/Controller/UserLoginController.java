package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Service.UserLoginService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lixy
 */
@RestController
@RequestMapping("/login")
@Api("用户登录相关")
public class UserLoginController {

    private UserLoginService userLoginService;
    @Autowired
    public void setUserLoginService(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @ApiOperation(value = "发送短信验证码" , notes = "发送短信验证码")
    @GetMapping("sendCode/{phone}")
    public ResponseData sendCode(
            @ApiParam(value = "电话号码" , name = "phoneNumber" , required = true)
            @PathVariable("phone") String phoneNumber
    ){
        return userLoginService.sendCode(phoneNumber);
    }

    @ApiOperation(value = "登录或注册(初次登录)",notes = "登录或注册(初次登录)")
    @PostMapping("lore")
    public ResponseData login(
//            @ApiParam(value = "用户电话号码",name = "phone", required = true)
//            @RequestParam("form.phone") String phoneNumber,
//            @ApiParam(value = "验证码",name = "verifyCode",required = true)
//            @RequestParam("form.code") String verifyCode
            @RequestBody Map<String , Object> paramsMap
            ){
        String phoneNumber = (String) paramsMap.get("phone");
        String verifyCode = (String) paramsMap.get("code");
        return userLoginService.login(phoneNumber,verifyCode);
    }
}
