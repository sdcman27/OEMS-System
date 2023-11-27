package edu.sru.thangiah.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for OpenAI API integration.
 * <p>
 * This class is marked with {@code @Configuration} to indicate that it provides configuration
 * settings to the Spring container. It is responsible for setting up a {@code RestTemplate}
 * that is pre-configured with the necessary headers for authentication to the OpenAI API.
 * </p>
 * <p>
 * The OpenAI API key is injected from the application properties file, enabling secure API requests.
 * </p>
 */
@Configuration
public class OpenAIConfig {
	
	/*
	 *____  __    __        _ _ 
	 / __ \/ /__ / /__ ___ (_|_)
	/ /_/ / / -_)  '_/(_-</ / / 
	\____/_/\__/_/\_\/___/_/_/  
	                        
	 */

    @Value("${openai.api.key}")
     String openaiApiKey;

    /**
     * Creates and configures a {@link RestTemplate} bean with an interceptor to add
     * the Authorization header to each HTTP request made to the OpenAI API.
     * <p>
     * This interceptor attaches an 'Authorization' header with a bearer token, which is the OpenAI API key,
     * to each outgoing request, ensuring that the client is authenticated for all interactions with the API.
     * </p>
     *
     * @return a pre-configured {@link RestTemplate} instance
     */
    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
    
}