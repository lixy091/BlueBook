package com.lixy.bluebook.DTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.lixy.bluebook.Entity.User;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixy
 */
@Data
public class UserDTO {
    private String nickName;
    private String icon;
    private Long id;
    public UserDTO(){}

    public UserDTO(User user){
        this.nickName = user.getNickName();
        this.icon = user.getIcon();
        this.id = user.getId();
    }

    public Map<String,Object> transMap(){
        return BeanUtil.beanToMap(this,new HashMap<>()
                , CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString()));
    }

}
