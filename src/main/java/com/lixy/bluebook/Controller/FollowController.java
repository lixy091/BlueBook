package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Service.FollowService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@RestController
@RequestMapping("/follow")
@Api(tags = "关注相关接口")
public class FollowController {
    @Resource
    private FollowService followService;

    @ApiOperation("查询是否关注")
    @GetMapping("/isFollow/{userId}")
    public ResponseData isFollow(
            @ApiParam(value = "id",name = "用户id",required = true)
            @PathVariable("userId") long id
    ){
        return followService.isFollow(id);
    }

    @ApiOperation("关注或取关")
    @PutMapping("/{userId}/{follow}")
    public ResponseData followUser(
            @ApiParam(value = "id",name = "用户id",required = true)
            @PathVariable("userId") long id,
            @ApiParam(value = "follow",name = "关注或取关",required = true)
            @PathVariable("follow") boolean follow
    ){
        return followService.followUser(id , follow);
    }

    @ApiOperation("共同关注")
    @GetMapping("/common/{id}")
    public ResponseData getCommonSub(
            @ApiParam(value = "id",name = "用户id",required = true)
            @PathVariable("id") long id
    ){
        return followService.getCommonSub(id);
    }
}
