package com.lixy.bluebook.Entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@Data
public class User {
    private Long id;
    private String phone;
    private String nickName;
    private String password;
    private String icon;
    private LocalDateTime creatTime;
    private LocalDateTime updateTime;
}
