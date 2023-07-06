package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.util.RandomUtil;
import com.lixy.bluebook.Dao.UserLoginMapper;
import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Service.UserLoginService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.LOGIN_PHONE;
import static com.lixy.bluebook.Utils.RegexPatterns.PHONE_REGEX;


/**
 * @author idt
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    private StringRedisTemplate redisTemplate;
    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Resource
    private UserLoginMapper userLoginMapper;

    @Override
    public ResponseData sendCode(String phoneNumber) {
        ResponseData responseData = null;
        //判断电话号码是否合法
        if (!phoneNumber.matches(PHONE_REGEX)){
            responseData = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+",电话号码不合法");
            return responseData;
        }
        //生成验证码
        String verifyCode = RandomUtil.randomNumbers(6);
        //将验证码保存至Redis
        redisTemplate.opsForValue().set(LOGIN_PHONE + phoneNumber , verifyCode,2, TimeUnit.MINUTES);
        //发送验证码
        System.out.println("验证码为:" + verifyCode);
        //返回信息
        responseData = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        return responseData;
    }

    @Override
    public ResponseData login(String phoneNumber, String verifyCode) {
        ResponseData responseData = null;
        String queryCode = redisTemplate.opsForValue().get(LOGIN_PHONE + phoneNumber);
        //校验验证码
        if (queryCode == null || !queryCode.equals(verifyCode)){
            responseData = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+",验证码错误");
            return responseData;
        }
        //根据手机查询用户

        //判断用户是否存在,不存在则保存用户信息至数据库

        //将用户信息保存至Redis

        //返回一个token给客户端
        return responseData;
    }
}