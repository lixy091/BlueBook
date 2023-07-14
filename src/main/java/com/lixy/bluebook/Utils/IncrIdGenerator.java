package com.lixy.bluebook.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author lixy
 */
@Component
public class IncrIdGenerator {

    private static final long BEGIN_TIMESTAMP = 1640995200L;

    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long generateIncrId(String keyPre){
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timeOffset = nowSecond - BEGIN_TIMESTAMP;

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

        long increment = stringRedisTemplate.opsForValue().increment("incr:"+keyPre + ":" + date);

        return timeOffset << 32 | increment;
    }
}
