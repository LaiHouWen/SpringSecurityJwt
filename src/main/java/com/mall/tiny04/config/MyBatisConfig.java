package com.mall.tiny04.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 配置
 */
@Configuration
@MapperScan(basePackages = {"com.mall.tiny04.mbg.mapper","com.mall.tiny04.dao"})
public class MyBatisConfig {

}
