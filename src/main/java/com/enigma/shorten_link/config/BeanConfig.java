package com.enigma.shorten_link.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class BeanConfig {
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
