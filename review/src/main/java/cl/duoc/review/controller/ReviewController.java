package cl.duoc.review.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.review.Dto.ApiResponse;
import cl.duoc.review.Dto.ReviewRequestDTO;
import cl.duoc.review.Dto.ReviewResponseDTO;
import cl.duoc.review.Dto.ReviewUpdateDTO;
import cl.duoc.review.service.AuthService;
import cl.duoc.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody ReviewRequestDTO request) {

        logger.info("Solicitud para crear reseña");

        authService.validateAuthorization(authorizationHeader);

        ReviewResponseDTO data = reviewService.createReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        201,
                        "Reseña creada correctamente",
                        data
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> findAll() {

        List<ReviewResponseDTO> data = reviewService.findAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseñas obtenidas correctamente",
                        data
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> findById(
            @PathVariable Long id) {

        ReviewResponseDTO data = reviewService.findById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseña obtenida correctamente",
                        data
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> findByUserId(
            @PathVariable Long userId) {

        List<ReviewResponseDTO> data = reviewService.findByUserId(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseñas del usuario obtenidas correctamente",
                        data
                )
        );
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> findByDestinationId(
            @PathVariable Long destinationId) {

        List<ReviewResponseDTO> data = reviewService.findByDestinationId(destinationId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseñas del destino obtenidas correctamente",
                        data
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> updateReview(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateDTO request) {

        authService.validateAuthorization(authorizationHeader);

        ReviewResponseDTO data = reviewService.updateReview(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseña actualizada correctamente",
                        data
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {

        authService.validateAuthorization(authorizationHeader);

        reviewService.deleteReview(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Reseña eliminada correctamente",
                        null
                )
        );
    }
}