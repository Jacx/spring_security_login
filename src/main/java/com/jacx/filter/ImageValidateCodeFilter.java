package com.jacx.filter;

import com.jacx.controller.ImageCodeController;
import com.jacx.exception.ValidateCodeException;
import com.jacx.properties.SecurityProperties;
import com.jacx.validate.ImageValidateCode;
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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Component
public class ImageValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(ImageValidateCodeFilter.class);
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private Set<String> urls = new HashSet<>();
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void afterPropertiesSet() throws ServletException {
        logger.info("初始化url.....");
        super.afterPropertiesSet();
        String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getImage().getUrls(), ",");
        if (strings != null) {
            urls.addAll(Arrays.asList(strings));
        }
        urls.add("/login");
    }

    @Autowired
    private AuthenticationFailureHandler kobeAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isMatch(request)) {
            logger.info("正在处理图形验证码");
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                kobeAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);

    }

    private boolean isMatch(HttpServletRequest request) {
        return urls.stream().anyMatch(s -> antPathMatcher.match(s, request.getRequestURI()));
    }

    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        String imageCode = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
        if (StringUtils.isBlank(imageCode)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        ImageValidateCode imageValidateCode = (ImageValidateCode) sessionStrategy.getAttribute(servletWebRequest, ImageCodeController.SESSION_KEY);
        if (null == imageValidateCode) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (imageValidateCode.isExpireIn()) {
            sessionStrategy.removeAttribute(servletWebRequest, ImageCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码过期");
        }
        if (!StringUtils.equals(imageCode, imageValidateCode.getCode())) {
            throw new ValidateCodeException("验证码不正确");
        }
        sessionStrategy.removeAttribute(servletWebRequest, ImageCodeController.SESSION_KEY);

    }
}

