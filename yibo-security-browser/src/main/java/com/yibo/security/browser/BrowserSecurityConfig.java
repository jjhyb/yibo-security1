package com.yibo.security.browser;

import com.yibo.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.yibo.security.core.properties.SecurityProperties;
import com.yibo.security.core.validate.code.ValidateCodeFilter;
import com.yibo.security.core.validate.code.sms.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author: huangyibo
 * @Date: 2019/7/28 0:33
 * @Description:
 */

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler yiBoAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler yiBoAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    //记住我功能，通过token查找到用户之后用于登录
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    /**
     * 对密码加盐加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置记住我功能的tokenRepository
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(dataSource);
        //在首次启动的时候创建表,后面启动需要注释掉不然会报错
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(yiBoAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(yiBoAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet();

        http.addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(validateCodeFilter,UsernamePasswordAuthenticationFilter.class)
        .formLogin()        //表单登录
            .loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form")
            .successHandler(yiBoAuthenticationSuccessHandler)//登录成功后使用自己实现的登录成功处理器
            .failureHandler(yiBoAuthenticationFailureHandler)//登录失败后使用自己实现的登录失败处理器
            .and()
        .rememberMe()   //记住我功能的配置
            .tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())//记住我选项的过期时间
            .userDetailsService(userDetailsService)//记住我功能，通过token查找到用户之后用于登录
        //http.httpBasic()    //弹出对话框登录
        .and()  //
        .authorizeRequests()//对请求做授权
            .antMatchers("/authentication/require",
                    securityProperties.getBrowser().getLoginPage(),
                    "/code/*").permitAll()   //访问这个url的时候不需要身份认证
            .anyRequest()       //任何请求
            .authenticated()    //都需要身份认证
        .and()
        .csrf().disable()  //关闭csrf功能
        .apply(smsCodeAuthenticationSecurityConfig);//将SmsCodeAuthenticationSecurityConfig的配置添加进来
    }
}
