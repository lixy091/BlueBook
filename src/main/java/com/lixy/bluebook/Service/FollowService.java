package com.lixy.bluebook.Service;

import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface FollowService {
    ResponseData isFollow(long id);

    ResponseData followUser(long id, boolean follow);

    ResponseData getCommonSub(long id);
}
