package com.lixy.bluebook.Config;

import com.lixy.bluebook.Interceptor.RefreshTokenInterceptor;
import com.lixy.bluebook.Interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**");
        registry.addInterceptor(new UserInterceptor()).excludePathPatterns(
                "/login/**"
                ,"/swagger-ui.html"
                ,"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
                ,"/doc.html/**","/error","/favicon.ico"

        );
    }
}
