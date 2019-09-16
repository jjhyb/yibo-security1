package com.yibo.security.core.validate.code.sms;

import com.yibo.security.core.validate.code.ValidateCode;
import com.yibo.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: huangyibo
 * @Date: 2019/7/30 23:46
 * @Description:
 */

/**
 * 短信验证码处理器
 */
@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String telephone = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "telephone");
        smsCodeSender.send(telephone, validateCode.getCode());
    }
}
