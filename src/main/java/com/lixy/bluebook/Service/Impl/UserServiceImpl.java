package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.UserMapper;
import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Service.UserService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author lixy
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseData getUserInfo(long id) {
        User userInfo = userMapper.getUserInfo(id);
        return userInfo == null
                ? ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"未找到该用户")
                : ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(userInfo);
    }

    @Override
    public ResponseData getUser() {
        ResponseData data;
        UserDTO user = UserLocal.getUserDTO();
        if (user == null){
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage());
            return data;
        }
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData(user);
        return data;
    }

    @Override
    public ResponseData sign() {
        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        String key = "sign:"+UserLocal.getUserDTO().getId() + new SimpleDateFormat(":yyyy:MM").format(new Date());
        Boolean isSigned = stringRedisTemplate.opsForValue().getBit(key, (long) day - 1);
        if (Boolean.TRUE.equals(isSigned)){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"今日已签到").setData(false);
        }
        stringRedisTemplate.opsForValue().setBit(key, (long) day - 1, true);
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(true);
    }

    @Override
    public ResponseData getSignCount() {
        int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
        String key = "sign:"+UserLocal.getUserDTO().getId() + new SimpleDateFormat(":yyyy:MM").format(new Date());
        List<Long> bitList = stringRedisTemplate.opsForValue().bitField(key, BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(day)).valueAt(0));
        if (bitList == null || bitList.isEmpty()){
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(0);
        }
        Long num = bitList.get(0);
        int count = 0;
        while ((num & 1) == 1){
            count++;
            num >>>= 1;
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(count);
    }
}
