package com.lixy.bluebook.Entity;

import com.lixy.bluebook.DTO.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@Data
@NoArgsConstructor
public class Blog {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商户id
     */
    private Long shopId;
    /**
     * 用户
     */
    private UserDTO user;
    /**
     * 是否点赞过了
     */
    private Boolean isLike;
    /**
     * 标题
     */
    private String title;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
    private String images;

    /**
     * 探店的文字描述
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
