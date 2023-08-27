package com.lixy.bluebook.Config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixy
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    String redisAddress;
    @Value("${spring.redis.port}")
    String redisPort;
    @Value("${spring.redis.password}")
    String redisPassword;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisAddress+":"+redisPort)
                .setPassword(redisPassword);
        return Redisson.create(config);
    }
}
