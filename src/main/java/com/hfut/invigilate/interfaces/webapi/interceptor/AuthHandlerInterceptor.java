package com.hfut.invigilate.interfaces.webapi.interceptor;

import com.hfut.invigilate.interfaces.model.dto.TokenDTO;
import com.hfut.invigilate.interfaces.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Resource
    TokenUtil tokenUtil;

    /**
     * 权限认证的拦截操作.
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        //为空直接禁止访问
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return false;
        }

        //todo 有直接验证token的，能验证过期时间等其他有用信息
        TokenDTO tokenDTO=null;
        try {
            tokenDTO=tokenUtil.parseToken(token);
        }catch (Exception e){//解析错误
            return false;
        }
        return true;
    }

}