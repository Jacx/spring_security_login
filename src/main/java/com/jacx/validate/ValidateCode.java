package com.jacx.validate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * validate code
 *
 * @author wjx
 * @date 2018/09/08
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class ValidateCode {
    private String code;
    private LocalDateTime expireIn;

    public ValidateCode(String code, LocalDateTime expireIn) {
        this.code = code;
        this.expireIn = expireIn;
    }

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireIn = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpireIn() {
        return LocalDateTime.now().isAfter(this.expireIn);
    }
}
