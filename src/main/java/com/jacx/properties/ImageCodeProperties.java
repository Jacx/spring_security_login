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
public class ImageCodeProperties {
    private int width=100;
    private int height=23;
    private int length=6;
    private int expireIn=60;
    private String urls;
}
