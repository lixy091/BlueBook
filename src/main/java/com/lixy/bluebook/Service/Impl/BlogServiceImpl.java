package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.util.StrUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.BlogMapper;
import com.lixy.bluebook.Entity.Blog;
import com.lixy.bluebook.Service.BlogService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ProjectConstant;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.lixy.bluebook.Utils.ProjectConstant.BLOG_LIKED;

/**
 * @author lixy
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseData uploadImage(MultipartFile image) {
        String filename = createNewFileName(image.getOriginalFilename());
        try {
            image.transferTo(new File(ProjectConstant.IMAGE_DIR , filename));
        } catch (IOException e) {
            log.error("图片保存失败" ,e);
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(filename);
    }

    @Override
    public ResponseData saveBlog(Blog blog) {
        UserDTO user = UserLocal.getUserDTO();
        blog.setUser(user);
        return blogMapper.saveBlog(blog) != 0
                ? ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("保存成功")
                : ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"保存失败");
    }

    @Override
    public ResponseData getBlog(long blogId) {
        Blog blog = blogMapper.getBlogById(blogId);
        if (blog == null){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"未找到该blog");
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(blog);
    }

    @Override
    public ResponseData getMyBlog(int currentPage, int pageSize) {
        ResponseData data;
        UserDTO user = UserLocal.getUserDTO();
        List<Blog> blogList = blogMapper.getBlogByUserId(user.getId(), (currentPage-1)*pageSize , pageSize);
        if (blogList == null || blogList.isEmpty()){
            data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
            data.setData(Collections.EMPTY_LIST);
            return data;
        }
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData(blogList);
        return data;
    }

    @Override
    public ResponseData getHotBlog(int currentPage, int pageSize) {
        List<Blog> blogs = blogMapper.getHotBlog(new RowBounds((currentPage-1)*pageSize , pageSize));
        if (blogs == null){
            blogs = Collections.emptyList();
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(blogs);
    }

    @Override
    public ResponseData likesBlog(long id) {
        UserDTO user = UserLocal.getUserDTO();
        String key = BLOG_LIKED+id;
        String delta = "";
        Double score = stringRedisTemplate.opsForZSet().score(key, user.getId().toString());
        if (score == null){
            delta = "1";
            stringRedisTemplate.opsForZSet().add(key , user.getId().toString() , System.currentTimeMillis());
            blogMapper.updateBlogLiked(id , delta);
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("点赞成功");
        }
        else {
            delta = "-1";
            stringRedisTemplate.opsForZSet().remove(key , user.getId().toString());
            blogMapper.updateBlogLiked(id , delta);
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("取消点赞成功");
        }

    }

    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(ProjectConstant.IMAGE_DIR, StrUtil.format("/blogs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format("/blogs/{}/{}/{}.{}", d1, d2, name, suffix);
    }

}
