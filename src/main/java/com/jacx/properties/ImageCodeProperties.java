package com.jacx.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * ImageCodeProperties
 *
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class ImageCodeProperties extends ValidateCodeProperties {
    public ImageCodeProperties() {
        setLength(4);
    }

    private int width = 100;
    private int height = 23;

}
