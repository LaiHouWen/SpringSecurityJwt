package com.mall.tiny04.component;

import com.mall.tiny04.dto.AdminUserDetails;
import com.mall.tiny04.mbg.model.UmsAdmin;
import com.mall.tiny04.mbg.model.UmsPermission;
import com.mall.tiny04.service.UmsAdminService;
import com.mall.tiny04.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * JWT登录授权过滤器
 * 在用户名和密码校验前添加的过滤器，如果请求中有jwt的token且有效，
 * 会取出token中的用户名，然后调用SpringSecurity的API进行登录操作。
 * 请求 前置 过滤 token
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER =LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        LOGGER.info("checking authHeader:{}",authHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)){
            //The part after "Bearer "
            String authToken = authHeader.substring(this.tokenHead.length());
            LOGGER.info("checking authToken:{}",authToken);
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}",username);
            //SecurityContextHolder.getContext().getAuthentication() == null
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               //
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    //打印用户的权限
                    LOGGER.info("authenticated Authorities:{}", userDetails.getAuthorities().size());
                    //这个需要设置
                    //如果没有这个会报 :Full authentication is required to access this resource
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

}
