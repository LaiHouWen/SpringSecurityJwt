package com.mall.tiny04.component;

import cn.hutool.json.JSONUtil;
import com.mall.tiny04.common.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当访问接口没有权限时，自定义的返回结果
 *
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        LOGGER.info("RestfulAccessDeniedHandler handle 当访问接口没有权限时，自定义的返回结果");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        //没有访问权限
        response.getWriter().println(JSONUtil.parse(CommonResult.failed(e.getMessage())));
        response.getWriter().flush();
    }

}
