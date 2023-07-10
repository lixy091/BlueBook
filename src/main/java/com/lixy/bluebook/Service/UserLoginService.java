package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Utils.ResponseData;

public interface UserLoginService {
    ResponseData sendCode(String phoneNumber);

    ResponseData login(String phoneNumber, String verifyCode);

    ResponseData loginByPassword(String phoneNumber , String password);
}
