package com.jacx.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Component
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {
    private Logger logger = LoggerFactory.getLogger(SmsValidateCodeProcessor.class);

    @Override
    public void sendValidateCode(ValidateCode validateCode, ServletWebRequest request) throws ServletRequestBindingException {
        logger.info("向{}发送手机验证码{}", ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile"), validateCode.getCode());


    }
}
