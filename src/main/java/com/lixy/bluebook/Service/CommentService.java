package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.Comment;
import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface CommentService {
    ResponseData getCommentsByBlog(long blogId);

    ResponseData postComment(Comment comment);

    ResponseData getCommentsByFC(long comId);

    ResponseData getMyComments();

    ResponseData deleteCommentById(long comId);
}
