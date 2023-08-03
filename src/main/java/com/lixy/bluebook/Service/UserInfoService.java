package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.UserInfo;
import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface UserInfoService {
    ResponseData saveUserInfo(UserInfo userInfo);
}
