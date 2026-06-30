package cl.duoc.review.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.review.Dto.TokenValidationResponseDTO;
import cl.duoc.review.exception.RemoteServiceException;

@Component
public class LoginClient {

    private final WebClient webClient;

    public LoginClient(@Qualifier("loginWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public TokenValidationResponseDTO validateToken(String authorizationHeader) {

        try {
            return webClient.get()
                    .uri("/api/v1/auth/validate")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .bodyToMono(TokenValidationResponseDTO.class)
                    .block();

        } catch (Exception e) {
            throw new RemoteServiceException("No se pudo validar el token con Login Service");
        }
    }
}
