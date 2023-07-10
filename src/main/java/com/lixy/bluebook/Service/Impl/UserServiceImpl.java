package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Service.UserService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.stereotype.Service;

/**
 * @author lixy
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public ResponseData getUserinfo() {
        return null;
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
