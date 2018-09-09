package com.jacx.support;

import lombok.Getter;
import lombok.Setter;

/**
 * SimpleResponse
 *
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class SimpleResponse {
    private Object content;

    public SimpleResponse(Object content) {
        this.content = content;
    }
}
