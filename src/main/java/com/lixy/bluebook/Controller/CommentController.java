package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Service.CommentService;
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
@Api(tags = "评论相关")
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @ApiOperation("根据博客获取评论")
    @GetMapping("/blog/{id}")
    public ResponseData getCommentsByBlog(
            @ApiParam(value = "blogId" , name = "博客id" , required = true)
            @PathVariable("id") long blogId
    ){
        //TODO
        return null;
    }

    @ApiOperation("根据一级评论获取评论")
    @GetMapping("/com/{id}")
    public ResponseData getCommentsByFC(
            @ApiParam(value = "comId" , name = "一级评论id" , required = true)
            @PathVariable("id") long comId
    ){
        //TODO
        return null;
    }

    @ApiOperation("获取自身评论")
    @GetMapping("/mine")
    public ResponseData getMyComments(){
        //TODO
        return null;
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/del/{id}")
    public ResponseData deleteCommentById(
            @ApiParam(value = "comId" , name = "评论id" , required = true)
            @PathVariable("id") long comId
    ){
        //TODO
        return null;
    }

}
