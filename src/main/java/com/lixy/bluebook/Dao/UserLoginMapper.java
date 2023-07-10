package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author lixy
 */
@Mapper
public interface UserLoginMapper {

    long saveUser(User user);

    User getUserByPhone(String phone);

}
