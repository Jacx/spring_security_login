package com.jacx.validate;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Component
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor<ImageValidateCode> {
    @Override
    public void sendValidateCode(ImageValidateCode validateCode, ServletWebRequest request) throws IOException {
        ImageIO.write(validateCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}
