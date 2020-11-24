package com.mall.tiny04.service.impl;

import com.mall.tiny04.common.CommonResult;
import com.mall.tiny04.service.RedisService;
import com.mall.tiny04.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * 会员管理Service实现类
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    //
    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i=0;i<6;i++){
            sb.append(random.nextInt(10));
        }
        //验证码 绑定手机号 并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE+telephone,sb.toString());
        redisService.expire(AUTH_CODE_EXPIRE_SECONDS+telephone,AUTH_CODE_EXPIRE_SECONDS);
        return CommonResult.success(sb.toString(),"获取验证码成功");
    }

    //对输入的验证码进行校验
    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (ObjectUtils.isEmpty(authCode)){
            return CommonResult.failed("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE+telephone);
        boolean result = authCode.equals(realAuthCode);
        if(result){
            return CommonResult.success(null,"验证码校验成功");
        }else {
            return CommonResult.failed("验证码不正确");
        }
    }


}


