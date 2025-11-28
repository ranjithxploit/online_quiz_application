package com.quiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to student login page
        registry.addRedirectViewController("/", "/login.html");
        registry.addRedirectViewController("/login", "/login.html");
        registry.addRedirectViewController("/admin", "/adminlogin.html");
    }
}
