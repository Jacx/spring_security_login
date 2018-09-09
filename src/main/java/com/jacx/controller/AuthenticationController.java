package com.jacx.controller;

import com.jacx.properties.SecurityProperties;
import com.jacx.support.SimpleResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationController
 *
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Controller
public class AuthenticationController {
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    // Spring 提供的工具类,会将跳转之前的请求缓存到session
    private RequestCache requestCache = new HttpSessionRequestCache();
    // 跳转工具类
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private SecurityProperties securityProperties;

    @RequestMapping("/authentication/require")
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public SimpleResponse authenticationRequire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            final String targetUri = savedRequest.getRedirectUrl();
            logger.info("targetUri = {}", targetUri);
            if (StringUtils.endsWithIgnoreCase(targetUri, ".html")) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getDef().getLoginPage());
            }
        }

        return new SimpleResponse("访问的服务需要身份认证,请引导用户到登录页面");

    }
}
