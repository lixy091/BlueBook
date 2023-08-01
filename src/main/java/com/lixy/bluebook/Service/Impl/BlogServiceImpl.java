package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.util.StrUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.BlogMapper;
import com.lixy.bluebook.Dao.FollowMapper;
import com.lixy.bluebook.Dao.UserMapper;
import com.lixy.bluebook.Entity.Blog;
import com.lixy.bluebook.Service.BlogService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ProjectConstant;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.lixy.bluebook.Utils.ProjectConstant.*;

/**
 * @author lixy
 */
@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private FollowMapper followMapper;

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
        if (blogMapper.saveBlog(blog) != 0){
            for (Long fansId : followMapper.getAllFansId(user.getId())) {
                stringRedisTemplate.opsForZSet().add(FOLLOWS_FEED+fansId , blog.getId().toString() , System.currentTimeMillis());
            }
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(blog.getId());
        }else {
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"保存失败");
        }
    }

    @Override
    public ResponseData getBlog(long blogId) {
        Blog blog = blogMapper.getBlogById(blogId);
        if (blog == null){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"未找到该blog");
        }
        Double score = stringRedisTemplate.opsForZSet().score(BLOG_LIKED + blogId, UserLocal.getUserDTO().getId().toString());
        blog.setIsLike(score != null && score >0);
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
        UserDTO user = UserLocal.getUserDTO();
        blogs.forEach(blog -> {
            Double score = stringRedisTemplate.opsForZSet().score(BLOG_LIKED + blog.getId(), user.getId().toString());
            blog.setIsLike(score != null && score >0);
        });
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(blogs);
    }

    @Override
    public ResponseData likeBlog(long id) {
        UserDTO user = UserLocal.getUserDTO();
        String key = BLOG_LIKED+id;
        String delta = "";
        Double score = stringRedisTemplate.opsForZSet().score(key, user.getId().toString());
        if (score == null){
            delta = "1";
            if (blogMapper.updateBlogLiked(id , delta) != 0){
                stringRedisTemplate.opsForZSet().add(key , user.getId().toString() , System.currentTimeMillis());
            }
        }
        else {
            delta = "-1";
            if (blogMapper.updateBlogLiked(id , delta) != 0){
                stringRedisTemplate.opsForZSet().remove(key , user.getId().toString());
            }
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(1);
    }

    @Override
    public ResponseData likingUserList(long id) {
        Set<String> top = stringRedisTemplate.opsForZSet().range(BLOG_LIKED+id , 0 ,4);
        if (top == null || top.size() == 0){
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(Collections.emptyList());
        }
        List<UserDTO> users = top.stream().map(s -> userMapper.getUserDTO(Long.parseLong(s))).collect(Collectors.toList());
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(users);
    }

    @Override
    public ResponseData getBlogsByUser(long id, int currentPage) {
        List<Blog> blogs = blogMapper.getBlogsByUser(id , new RowBounds((currentPage-1)*10 , 10));
        if (blogs == null){
            blogs = Collections.emptyList();
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(blogs);
    }

    @Override
    public ResponseData getBlogsOfFollow(long max, Integer offset) {
        long userId = UserLocal.getUserDTO().getId();
        Map<String , Object> resultMap = new HashMap<>();
        resultMap.put("list" , Collections.emptyList());
        resultMap.put("minTime" , BLANK_OBJECT);
        resultMap.put("offset" , BLANK_OBJECT);
        List<Long> idList = new ArrayList<>();
        long minTime  = 0L;
        int offsetNext = 1;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(FOLLOWS_FEED + userId, 0, max, offset, 5);
        if (typedTuples == null || typedTuples.isEmpty()){
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(resultMap);
        }
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples){
            idList.add(Long.parseLong(typedTuple.getValue()));
            if (minTime == typedTuple.getScore().longValue()){
                offsetNext++;
            }else {
                offsetNext = 1;
            }
            minTime = typedTuple.getScore().longValue();
        }
        List<Blog> blogs = idList.stream().map(blogMapper::getBlogById).collect(Collectors.toList());
        //TODO blogs待处理 , 接口未测试
        resultMap.put("list" , blogs);
        resultMap.put("minTime" , minTime);
        resultMap.put("offset" , offsetNext);
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(resultMap);
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
