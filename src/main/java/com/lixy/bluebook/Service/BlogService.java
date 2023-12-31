package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.Blog;
import com.lixy.bluebook.Utils.ResponseData;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lixy
 */
public interface BlogService {
    ResponseData uploadImage(MultipartFile image);

    ResponseData saveBlog(Blog blog);

    ResponseData getBlog(long blogId);

    ResponseData getMyBlog(int currentPage, int pageSize);

    ResponseData getHotBlog(int currentPage, int pageSize);

    ResponseData likeBlog(long id);

    ResponseData likingUserList(long id);

    ResponseData getBlogsByUser(long id, int currentPage);

    ResponseData getBlogsOfFollow(long max, Integer offset);
}
