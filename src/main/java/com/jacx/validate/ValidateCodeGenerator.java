package com.jacx.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
public interface ValidateCodeGenerator {
    ValidateCode generate(ServletWebRequest request);
}
