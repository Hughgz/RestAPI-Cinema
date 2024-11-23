package com.example.API_Cinema.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dahzoj4fy");
        config.put("api_key", "884148763248879");
        config.put("api_secret", "a6dL6HNdCJBOryZU46zTfaRyH-E");
        return new Cloudinary(config);
    }
}