package com.jacx.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * DefaultPrpperties
 *
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Setter
@Getter
public class DefaultProperties {
    private String loginPage = "/login.html";
    private LoginType loginType = LoginType.JSON;
}
