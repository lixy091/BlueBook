package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.Blog;
import com.lixy.bluebook.Service.BlogService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Api(tags = "博客相关")
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Resource
    private BlogService blogService;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public ResponseData uploadImage(
            @ApiParam(value = "image" , name = "图片" , required = true)
            @RequestParam("file") MultipartFile image
            ){
        return blogService.uploadImage(image);
    }

    @ApiOperation("保存blog")
    @PostMapping("/save")
    public ResponseData saveBlog(
            @ApiParam(value = "blog",name = "博客",required = true)
            @RequestBody Blog blog
            ){
        return blogService.saveBlog(blog);
    }

    @ApiOperation("查询blog")
    @GetMapping("/{id}")
    public ResponseData getBlog(
            @ApiParam(value = "blogId" , name = "blogId" ,required = true)
            @PathVariable("id") long blogId
    ){
        return blogService.getBlog(blogId);
    }

    @ApiOperation("查询自己的blog")
    @GetMapping("/mine")
    public ResponseData getMyBlog(
            @ApiParam(value = "currentPage" , name = "当前页码")
            @RequestParam(value = "currentPage" , defaultValue = "1") int currentPage,
            @ApiParam(value = "pageSize" , name = "每页条数")
            @RequestParam(value = "pageSize" , defaultValue = "10") int pageSize
    ){
        return blogService.getMyBlog(currentPage , pageSize);
    }

    @ApiOperation("首页博客")
    @GetMapping("hot")
    public ResponseData getHotBlog(
            @ApiParam(value = "currentPage" , name = "当前页码")
            @RequestParam(value = "currentPage" , defaultValue = "1") int currentPage,
            @ApiParam(value = "pageSize" , name = "每页条数")
            @RequestParam(value = "pageSize" , defaultValue = "10") int pageSize
    ){
        return blogService.getHotBlog(currentPage , pageSize);
    }

    @ApiOperation("点赞博客")
    @PutMapping("/like/{id}")
    public ResponseData likesBlog(
            @ApiParam(value = "id",name = "博客id",required = true)
            @PathVariable("id") long id
    ){
        return blogService.likeBlog(id);
    }

    @ApiOperation("查询前五点赞列表")
    @GetMapping("/likes/{id}")
    public ResponseData likingUserList(
            @ApiParam(value = "id",name = "博客id",required = true)
            @PathVariable("id") long id
    ){
        return blogService.likingUserList(id);
    }

    @ApiOperation("根据用户id查询博客")
    @GetMapping("/ofUser")
    public ResponseData getBlogsByUser(
            @ApiParam(value = "id",name = "博客id",required = true)
            @RequestParam("id") long id,
            @ApiParam(value = "currentPage" , name = "当前页码")
            @RequestParam(value = "current" , defaultValue = "1") int currentPage
    ){
        return blogService.getBlogsByUser(id , currentPage);
    }

    @ApiOperation("关注列表的博客")
    @GetMapping("/ofFollow")
    public ResponseData getBlogsOfFollow(
            @ApiParam(value = "" ,name = "",required = true)
            @RequestParam("max") long max,
            @ApiParam(value = "" , name = "" )
            @RequestParam(value = "offset" , defaultValue = "0") Integer offset
    ){
        return blogService.getBlogsOfFollow(max , offset);
    }

}
