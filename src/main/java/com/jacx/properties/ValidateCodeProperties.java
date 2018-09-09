package com.jacx.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class ValidateCodeProperties {
    private int length=6;
    private int expireIn=60;
    private String urls;
}
