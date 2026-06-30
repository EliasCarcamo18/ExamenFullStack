package cl.duoc.review.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.review.Dto.DestinationResponseDTO;
import cl.duoc.review.exception.RemoteServiceException;

@Component
public class DestinationClient {

    private final WebClient webClient;

    public DestinationClient(@Qualifier("destinationWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public DestinationResponseDTO getDestinationById(Long destinationId) {

        try {
            return webClient.get()
                    .uri("/api/v1/destinations/" + destinationId)
                    .retrieve()
                    .bodyToMono(DestinationResponseDTO.class)
                    .block();

        } catch (Exception e) {
            throw new RemoteServiceException("No se pudo validar el destino con Destination Service");
        }
    }
}
