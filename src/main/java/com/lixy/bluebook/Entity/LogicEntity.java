package com.lixy.bluebook.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogicEntity {
    private Object data;
    private LocalDateTime expireTime;

}
