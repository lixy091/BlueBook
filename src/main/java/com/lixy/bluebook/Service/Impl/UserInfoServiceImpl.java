package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.Dao.UserInfoMapper;
import com.lixy.bluebook.Entity.UserInfo;
import com.lixy.bluebook.Service.UserInfoService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Override
    public ResponseData saveUserInfo(UserInfo userInfo) {
        userInfo.setUserId(UserLocal.getUserDTO().getId());
        return userInfoMapper.updateUserInfoById(userInfo) != 0
                ? ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(1)
                : ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage());
    }
}
