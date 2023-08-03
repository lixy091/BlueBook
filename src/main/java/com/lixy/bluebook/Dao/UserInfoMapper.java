package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface UserInfoMapper {

    long addUserInfo(long userId);

    long updateUserInfoById(UserInfo userInfo);
}
