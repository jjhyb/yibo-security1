package com.yibo.security.browser.support;

/**
 * @author: huangyibo
 * @Date: 2019/7/28 21:22
 * @Description:
 */
public class SimpleResponse {

    private Object content;

    public SimpleResponse(Object content){
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
