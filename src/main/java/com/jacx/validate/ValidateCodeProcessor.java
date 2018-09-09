package com.jacx.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
public interface ValidateCodeProcessor {
    String SESSION_KEY = "SESSION_KEY_";
    /**
     * 创建验证码
     * @param request
     */
    void createValidateCode(ServletWebRequest request) throws Exception;
    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void validate(ServletWebRequest servletWebRequest);
}
