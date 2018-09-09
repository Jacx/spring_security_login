package com.jacx.config;

import com.jacx.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityPropertiesConfig
 *
 * @author wjx
 * @date 2018/09/09
 */
@SuppressWarnings("unused")
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityPropertiesConfig {
}
