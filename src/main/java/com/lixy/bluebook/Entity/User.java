package com.lixy.bluebook.Entity;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String phone;
    private String nickName;
    private String password;
    private String icon;
    private LocalDateTime creatTime;
    private LocalDateTime updateTime;

    {
        nickName ="user_" + RandomUtil.randomString(12);
        password = "";
        icon = "";
        creatTime = LocalDateTime.now();
        updateTime = creatTime;
    }

    public User(String phone){
        this.phone = phone;
    }

}
