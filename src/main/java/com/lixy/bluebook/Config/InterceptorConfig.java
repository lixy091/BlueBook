package com.lixy.bluebook.Config;

import com.lixy.bluebook.Interceptor.RefreshTokenInterceptor;
import com.lixy.bluebook.Interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lixy
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new UserInterceptor()).excludePathPatterns("/login/sendCode"
                ,"/login/lore");
    }
}
