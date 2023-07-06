package com.lixy.bluebook.Utils;

import com.lixy.bluebook.DTO.UserDTO;

/**
 * @author lixy
 */
public class UserLocal {
    public static final ThreadLocal<UserDTO> THREAD_LOCAL = new ThreadLocal<>();

    public static UserDTO getUserDTO(){
        return THREAD_LOCAL.get();
    }

    public static void setUserDTO(UserDTO userDTO){
        THREAD_LOCAL.set(userDTO);
    }

    public static void removeUserDTO(){
        THREAD_LOCAL.remove();
    }
}
