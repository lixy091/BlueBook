package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lixy
 */
@Mapper
public interface CommentMapper {

    List<Comment> getCommentsByBLog(long blogId);

    List<Comment> getCommentsByIds(List<Long> ids);

    long insertComment(Comment comment);

    List<Comment> getCommentsByFC(long comId);

}
