package com.lixy.bluebook.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Entity.User;
import com.lixy.bluebook.Utils.UserLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.USER_INFO;

/**
 * @author lixy
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("authorization");
        if (token == null || "".equals(token)){
            return true;
        }
        //查询Redis用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(USER_INFO + token);
        if (userMap.isEmpty()) {
            return true;
        }
        User user = BeanUtil.fillBeanWithMap(userMap , new User() , false);
        //保存到ThreadLocal
        UserLocal.setUserDTO(new UserDTO(user));
        //刷新token有效期
        stringRedisTemplate.expire(USER_INFO+token,2, TimeUnit.HOURS);
        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserLocal.removeUserDTO();
    }
}
