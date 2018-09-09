package com.jacx.validate;

import com.jacx.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {
    //    依赖搜索
    //收集容器中所有ValidateCodeGenerator的实现
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 根据请求的URL获取校验码类型
     *
     * @param request
     * @return
     */
    private String getProcessorType(ServletWebRequest request) {
        return StringUtils.substringAfter(request.getRequest().getRequestURI(), "/code/");
    }

    private T generateValidateCode(ServletWebRequest request) {
        String processorType = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(processorType + "ValidateCodeGeneratorImpl");
        T validateCode = (T) validateCodeGenerator.generate(request);
        return validateCode;
    }

    private void saveValidateCode(T validateCode, ServletWebRequest request) {
        sessionStrategy.setAttribute(request, SESSION_KEY + getProcessorType(request).toUpperCase(), validateCode);

    }

    public abstract void sendValidateCode(T validateCode, ServletWebRequest request) throws Exception;

    @Override
    public void createValidateCode(ServletWebRequest request) throws Exception {
        T validateCode = generateValidateCode(request);
        saveValidateCode(validateCode, request);
        sendValidateCode(validateCode, request);


    }

    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY + getValidateCodeType(request).toString().toUpperCase();
    }

    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType processorType = getValidateCodeType(request);
        String sessionKey = getSessionKey(request);
        T codeInSession = (T) sessionStrategy.getAttribute(request, sessionKey);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException(processorType + "验证码不存在");
        }

        if (codeInSession.isExpireIn()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCodeException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException(processorType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);

    }
}
