package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.UserMapper;
import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Service.UserService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
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
}
