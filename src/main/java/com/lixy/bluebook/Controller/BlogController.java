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

    @ApiOperation("查询blog详情")
    @GetMapping("/info/{id}")
    public ResponseData getBlog(
            @ApiParam(value = "" , name = "" ,required = true)
            @PathVariable("id") long blogId
    ){
        return blogService.getBlog(blogId);
    }
}
