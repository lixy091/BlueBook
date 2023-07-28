package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Blog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface BlogMapper {

    long saveBlog(Blog blog);

    Blog getBlogById(long id);
}
