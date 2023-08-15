package com.ezen.valuefinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URL;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    ClassLoader classLoader = getClass().getClassLoader();
    URL url = classLoader.getResource("");
    String uploadPath = url.getPath();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
