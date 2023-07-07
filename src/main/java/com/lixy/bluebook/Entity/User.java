package com.lixy.bluebook.Entity;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@ApiModel("用户实体")
@NoArgsConstructor
@Data
public class User {
    @ApiModelProperty(value = "用户ID" , name = "id")
    private Long id;
    @ApiModelProperty(value = "用户电话号码" , name = "phone" , required = true)
    private String phone;
    @ApiModelProperty(value = "昵称" , name = "nickName")
    private String nickName;
    private String password;
    @ApiModelProperty(hidden = true)
    private String icon;
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    {
        nickName ="user_" + RandomUtil.randomString(12);
        password = "";
        icon = "";
        createTime = LocalDateTime.now();
        updateTime = createTime;
    }

    public User(String phone){
        this.phone = phone;
    }

}
