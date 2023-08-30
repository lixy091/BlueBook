package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Dao.CommentMapper;
import com.lixy.bluebook.Entity.Comment;
import com.lixy.bluebook.Service.CommentService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import jdk.jfr.Unsigned;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lixy.bluebook.Utils.ProjectConstant.COMMENT_SHOP;

/**
 * @author lixy
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseData getCommentsByBlog(long blogId) {
        List<Comment> comments = null;
        //获取redis中的CommentId
        String key = COMMENT_SHOP+blogId;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        //如果redis中为空则用blogId去查找数据库
        if (members == null || members.isEmpty()){
            comments = commentMapper.getCommentsByBLog(blogId);
            //若查找到数据库则将id写入redis中
            if (comments != null){
                stringRedisTemplate.opsForSet().add(key , comments.stream().map(comment -> comment.getId().toString()).toArray(String[]::new));
                stringRedisTemplate.expire(key , 30L , TimeUnit.MINUTES);
            }
        }else {
            //若能找到则用id去数据库进行查找
            List<Long> ids = members.stream().map(Long::parseUnsignedLong).collect(Collectors.toList());
            comments = commentMapper.getCommentsByIds(ids);
            stringRedisTemplate.expire(key , 30L , TimeUnit.MINUTES);
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage())
                .setData(comments == null ? Collections.emptyList() : comments);
    }

    @Override
    public ResponseData postComment(Comment comment) {
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(commentMapper.insertComment(comment));
    }

    @Override
    public ResponseData getCommentsByFC(long comId) {
        List<Comment> comments = commentMapper.getCommentsByFC(comId);
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(Objects.requireNonNullElse(comments, Collections.emptyList()));
    }

    @Override
    public ResponseData getMyComments() {
        List<Comment> comments = commentMapper.getCommentsByUserId(UserLocal.getUserDTO().getId());
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(Objects.requireNonNullElse(comments, Collections.emptyList()));
    }

    @Override
    public ResponseData deleteCommentById(long comId) {
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(commentMapper.deleteCommentById(comId));
    }
}
