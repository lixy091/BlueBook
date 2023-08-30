package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.Comment;
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
    @GetMapping("blog/{blogId}")
    public ResponseData getCommentsByBlog(
            @ApiParam(value = "博客id" , name = "blogId" , required = true)
            @PathVariable("blogId") long blogId
    ){
        return commentService.getCommentsByBlog(blogId);
    }

    @ApiOperation("根据一级评论获取评论")
    @GetMapping("/com/{comId}")
    public ResponseData getCommentsByFC(
            @ApiParam(value = "一级评论id" , name = "comId" , required = true)
            @PathVariable("comId") long comId
    ){
        return commentService.getCommentsByFC(comId);
    }

    @ApiOperation("获取自身评论")
    @GetMapping("/mine")
    public ResponseData getMyComments(){
        return commentService.getMyComments();
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/del/{comId}")
    public ResponseData deleteCommentById(
            @ApiParam(value = "评论id" , name = "comId" , required = true)
            @PathVariable("comId") long comId
    ){
        return commentService.deleteCommentById(comId);
    }

    @ApiOperation("发评论")
    @PostMapping("/comment")
    public ResponseData postComment(
            @ApiParam(value = "评论" , name = "comment", required = true)
            @RequestBody Comment comment
            ){
        return commentService.postComment(comment);
    }

}
