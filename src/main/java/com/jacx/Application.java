package com.jacx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * application bootstrap
 *
 * @author wjx
 * @date 2018/09/07
 */
@SuppressWarnings("unused")
@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @GetMapping("/me")
    public Object getMe(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}
