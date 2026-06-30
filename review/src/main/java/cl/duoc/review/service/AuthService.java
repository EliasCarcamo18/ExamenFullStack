package cl.duoc.review.service;

import org.springframework.stereotype.Service;
import cl.duoc.review.client.LoginClient;
import cl.duoc.review.Dto.TokenValidationResponseDTO;
import cl.duoc.review.exception.InvalidReviewException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginClient loginClient ;

    public void validateAuthorization(String authorizationHeader) {

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new InvalidReviewException("El token de autorizacion es obligatorio");
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidReviewException("El token debe comenzar con Bearer");
        }

        TokenValidationResponseDTO response = loginClient.validateToken(authorizationHeader);

        if (response == null || response.getValid() == null || !response.getValid()) {
            throw new InvalidReviewException("Su token es invalido oa expirado");
        }
    }
}