package com.jacx.controller;

import com.jacx.properties.SecurityProperties;
import com.jacx.validate.ImageValidateCode;
import com.jacx.validate.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Controller
public class ImageCodeController {
    public final static String SESSION_KEY = "SESSION_IMG_KEY";
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ValidateCodeGenerator validateCodeGenerator;
    @GetMapping("/code/image")
    public void generateImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageValidateCode imageValidateCode = (ImageValidateCode) validateCodeGenerator.generate(new ServletWebRequest(request, response));
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageValidateCode);
        ImageIO.write(imageValidateCode.getImage(), "JPEG", response.getOutputStream());


    }

}
