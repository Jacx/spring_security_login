package com.jacx.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SecurityProperties
 *
 * @author wjx
 * @date 2018/09/07
 */
@SuppressWarnings("unused")
@Component
@ConfigurationProperties(prefix = "kobe.security")
@Setter
@Getter
public class SecurityProperties {
    private ImageCodeProperties image = new ImageCodeProperties();
    private DefaultProperties def = new DefaultProperties();
}
