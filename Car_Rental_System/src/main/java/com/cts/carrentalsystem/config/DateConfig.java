package com.cts.carrentalsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DateConfig {
	    // Configure ObjectMapper to handle Java 8 date/time types
	    @Bean
	    public ObjectMapper objectMapper() {
	        ObjectMapper mapper = new ObjectMapper();
	        JavaTimeModule javaTimeModule = new JavaTimeModule();
	        mapper.registerModule(javaTimeModule);
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        return mapper;
	    }
}



