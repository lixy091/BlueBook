package com.lixy.bluebook.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@Data
@NoArgsConstructor
public class Follow {
    private long id;
    private long userId;
    private long followUserId;
    private LocalDateTime createTime;

    public Follow(long userId , long followUserId){
        this.userId = userId;
        this.followUserId = followUserId;
    }
}
