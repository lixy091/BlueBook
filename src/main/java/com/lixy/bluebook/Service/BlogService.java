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
}
