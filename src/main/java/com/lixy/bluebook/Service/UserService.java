package com.lixy.bluebook.Service;

import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface UserService {
    ResponseData getUserInfo(long id);
    ResponseData getUser();

    ResponseData sign();

    ResponseData getSignCount();
}
