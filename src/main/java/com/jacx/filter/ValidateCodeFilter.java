package com.jacx.filter;

import com.jacx.exception.ValidateCodeException;
import com.jacx.properties.SecurityProperties;
import com.jacx.smsAuthentication.ValidateCodeProcessorHolder;
import com.jacx.validate.ValidateCodeType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(ValidateCodeFilter.class);
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
    @Autowired
    private AuthenticationFailureHandler kobeAuthenticationFailureHandler;
    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void afterPropertiesSet() {
        urlMap.put("/authentication/form", ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getImage().getUrls(), ValidateCodeType.IMAGE);

        urlMap.put("/authentication/mobile", ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getValid().getUrls(), ValidateCodeType.SMS);
    }

    /**
     * 将系统中配置的需要校验验证码的URL根据校验的类型放入map
     *
     * @param urlString
     * @param type
     */
    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeType(request);
        if (type != null) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(request, response));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                kobeAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }


    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                    break;
                }
            }
        }
        return result;
    }


}

