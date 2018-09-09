package com.jacx.config;

import com.jacx.filter.ValidateCodeFilter;
import com.jacx.properties.SecurityProperties;
import com.jacx.smsAuthentication.SmsSecurityConfig;
import com.jacx.validate.ImageValidateCodeGeneratorImpl;
import com.jacx.validate.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * SecurityConfig
 *
 * @author wjx
 * @date 2018/09/07
 */
@SuppressWarnings("unused")
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private AuthenticationSuccessHandler kobeAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler kobeAuthenticationFailureHandler;
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService kobeUserDetailServiceImpl;
    @Autowired
    private SmsSecurityConfig smsSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/authentication/require").loginProcessingUrl("/authentication/form")
                .successHandler(kobeAuthenticationSuccessHandler)
                .failureHandler(kobeAuthenticationFailureHandler)
                .and()
                /*记住我*/
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(3600)
                .userDetailsService(kobeUserDetailServiceImpl)
                .and()
                .authorizeRequests()
                .antMatchers(securityProperties.getDef().getLoginPage(), "/authentication/require", "/code/*").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .apply(smsSecurityConfig);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(ValidateCodeGenerator.class)
    public ValidateCodeGenerator validateCodeGenerator(SecurityProperties securityProperties) {
        return new ImageValidateCodeGeneratorImpl(securityProperties);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        启动的时候创建表
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


}
