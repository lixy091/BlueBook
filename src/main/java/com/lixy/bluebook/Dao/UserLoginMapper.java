package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.User;

import java.util.Map;

/**
 * @author lixy
 */
public interface UserLoginMapper {

    long saveUser(User user);

    User getUserByPhone(String phone);
}
