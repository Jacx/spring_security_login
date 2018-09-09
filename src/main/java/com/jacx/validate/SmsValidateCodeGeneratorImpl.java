package com.jacx.validate;

import com.jacx.properties.SecurityProperties;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Component
public class SmsValidateCodeGeneratorImpl implements ValidateCodeGenerator {
    private SecurityProperties securityProperties;

    public SmsValidateCodeGeneratorImpl(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
               .withinRange('0','9').build();
        String randomLetters = generator.generate(securityProperties.getValid().getLength());
        return new ValidateCode(randomLetters, securityProperties.getValid().getExpireIn());
    }


}
