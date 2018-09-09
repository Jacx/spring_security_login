package com.jacx.controller;

import com.jacx.properties.SecurityProperties;
import com.jacx.validate.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Controller
public class ValidateCodeController {
    public final static String SESSION_KEY = "SESSION_IMG_KEY";
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessor;

    @GetMapping("/code/{type}")
    public void generateImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        validateCodeProcessor.get(type + "ValidateCodeProcessor").createValidateCode(new ServletWebRequest(request, response));


    }

}
