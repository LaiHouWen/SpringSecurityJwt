package com.mall.tiny04.config;

import com.mall.tiny04.component.JwtAuthenticationTokenFilter;
import com.mall.tiny04.component.RestAuthenticationEntryPoint;
import com.mall.tiny04.component.RestfulAccessDeniedHandler;
import com.mall.tiny04.dto.AdminUserDetails;
import com.mall.tiny04.mbg.model.UmsAdmin;
import com.mall.tiny04.mbg.model.UmsPermission;
import com.mall.tiny04.service.UmsAdminService;
import com.mall.tiny04.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


/**
 * SpringSecurity的配置
 *
 * UserDetails：SpringSecurity定义用于封装用户信息的类（主要是用户信息和权限），
 * 需要自行实现；
 */
@Configuration
//开启Security
@EnableWebSecurity
//保证post之前的注解可以使用
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    /**
     * 用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器；
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                //由于使用的是JWT，我们这里不需要csrf
                .disable()//
                //基于token，所以不需要session
                .sessionManagement()
                //STATELESS不创建httpsession并不使用
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and()
                .authorizeRequests()
                //允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs")
                .permitAll()
                //对登录注册要允许匿名访问
                .antMatchers("/admin/login",
                        "/admin/register")
                .permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                //测试时全部运行访问
//                .antMatchers("/**")
//                .permitAll()
                //除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated();
        //禁用缓存
        http.headers().cacheControl();
        //添加JWT filter 前置拦截
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    /**
     * 用于配置 UserDetailsService 及PasswordEncoder；
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * SpringSecurity定义的用于对密码进行编码及比对的接口，
     * 目前使用的是BCryptPasswordEncoder；
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * SpringSecurity定义的核心接口，用于根据用户名获取用户信息，需要自行实现；
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        //获取登录用户信息,查询用户权限
//        return username -> {
//            UmsAdmin admin = adminService.getAdminByUsername(username);
//            if (admin !=null ){
//                List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());
//                return new AdminUserDetails(admin,permissionList);
//            }
//            throw new UsernameNotFoundException("用户名或密码错误");
//        };
        return new UserDetailsServiceImpl();
    }

    /**
     * 在用户名和密码校验前添加的过滤器，
     * 如果有jwt的token，会自行根据token信息进行登录。
     * @return
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
