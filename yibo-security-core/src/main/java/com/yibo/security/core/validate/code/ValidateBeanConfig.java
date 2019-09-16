package com.yibo.security.core.validate.code;

import com.yibo.security.core.properties.SecurityProperties;
import com.yibo.security.core.validate.code.image.ImageCodeGenerator;
import com.yibo.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.yibo.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: huangyibo
 * @Date: 2019/7/29 22:12
 * @Description:
 */

@Configuration
public class ValidateBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    //如果spring容器中不存在imageCodeGenerator的时候，才调用此方法
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    //如果spring容器中不存在SmsCodeSender的实现类的时候，才调用此方法
    public SmsCodeSender smsCodeSender(){
        return new DefaultSmsCodeSender();
    }
}
