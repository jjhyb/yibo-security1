package com.yibo.security.core.properties;

/**
 * @author: huangyibo
 * @Date: 2019/7/28 21:39
 * @Description:
 */
public class BrowserProperties {

    private String loginPage = "/yibo-signIn.html";

    private LoginType loginType = LoginType.JSON;

    //登录页面记住我的过期时间，秒为单位，这里为1小时
    private int rememberMeSeconds = 3600;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
