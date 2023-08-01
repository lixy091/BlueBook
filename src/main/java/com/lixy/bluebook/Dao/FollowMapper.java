package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lixy
 */
@Mapper
public interface FollowMapper {

    long getIsFollowByUserId(@Param("id") long id ,@Param("fid") long fid);

    long insertFollow(Follow follow);

    long deleteFollow(@Param("id") long id ,@Param("fid") long fid);

    List<Long> getAllFansId(long id);
}
