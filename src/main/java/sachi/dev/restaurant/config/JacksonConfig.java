package sachi.dev.restaurant.config;

import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        // Create a new ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule to handle Java 8 date and time types
        objectMapper.registerModule(new JavaTimeModule());

        // Disable the feature that writes dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}
