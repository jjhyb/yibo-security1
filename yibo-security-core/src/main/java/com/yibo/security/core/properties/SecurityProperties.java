package com.yibo.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: huangyibo
 * @Date: 2019/7/28 21:39
 * @Description:
 */

@ConfigurationProperties(prefix = "yibo.security")
//上面注解会读取"yibo.security"开头的配置文件
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
