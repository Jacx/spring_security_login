package com.jacx.validate;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * 短信验证码
 *
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Setter
@Getter
public class ImageValidateCode extends ValidateCode {
    private BufferedImage image;

    public ImageValidateCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }
}
