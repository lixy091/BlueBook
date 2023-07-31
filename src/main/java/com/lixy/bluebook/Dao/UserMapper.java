package com.lixy.bluebook.Dao;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface UserMapper {
    User getUserInfo(long id);

    UserDTO getUserDTO(long id);
}
