package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author lixy
 */
@Mapper
public interface BlogMapper {

    long saveBlog(Blog blog);

    Blog getBlogById(long id);

    List<Blog> getBlogByUserId(@Param("id") long id , @Param("currentNum") int currentNum , @Param("pageSize") int pageSize);

    List<Blog> getHotBlog(RowBounds rowBounds);

    long updateBlogLiked(@Param("id") long id , @Param("delta") String delta);

    List<Blog> getBlogsByUser(@Param("userId") long userId , RowBounds rowBounds);
}
