package cl.duoc.review.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("loginWebClient")
    @LoadBalanced
    public WebClient loginWebClient() {
        return WebClient.builder()
                .baseUrl("http://login")
                .build();
    }

    @Bean("destinationWebClient")
    @LoadBalanced
    public WebClient destinationWebClient() {
        return WebClient.builder()
                .baseUrl("http://destination")
                .build();
    }
}