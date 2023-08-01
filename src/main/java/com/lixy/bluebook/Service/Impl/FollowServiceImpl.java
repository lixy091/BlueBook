package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.FollowMapper;
import com.lixy.bluebook.Dao.UserMapper;
import com.lixy.bluebook.Entity.Follow;
import com.lixy.bluebook.Service.FollowService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lixy.bluebook.Utils.ProjectConstant.FOLLOWS;

/**
 * @author lixy
 */
@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    private FollowMapper followMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseData isFollow(long id) {
        Boolean isFollow = followMapper.getIsFollowByUserId(UserLocal.getUserDTO().getId(), id) != 0;
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(isFollow);
    }

    @Override
    public ResponseData followUser(long id, boolean follow) {
        Long userId = UserLocal.getUserDTO().getId();
        if (follow){
            if (followMapper.insertFollow(new Follow(userId , id)) != 0){
                stringRedisTemplate.opsForSet().add(FOLLOWS+ userId, String.valueOf(id));
                return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("关注成功");
            }
        }else {
            if (followMapper.deleteFollow(userId, id) !=0){
                stringRedisTemplate.opsForSet().remove(FOLLOWS+userId , String.valueOf(id));
                return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("取关成功");
            }
        }
        return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage());
    }

    @Override
    public ResponseData getCommonSub(long id) {
        long userId = UserLocal.getUserDTO().getId();
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(FOLLOWS+userId , FOLLOWS+id);
        if (intersect == null || intersect.size() == 0){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()).setData(Collections.emptyList());
        }
        List<UserDTO> commonSubUser = intersect.stream().map(sid -> userMapper.getUserDTO(Long.parseLong(sid))).collect(Collectors.toList());
        return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()).setData(commonSubUser);
    }
}
