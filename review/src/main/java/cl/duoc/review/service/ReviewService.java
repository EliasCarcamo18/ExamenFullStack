package cl.duoc.review.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import cl.duoc.review.client.DestinationClient;
import cl.duoc.review.Dto.DestinationResponseDTO;
import cl.duoc.review.Dto.ReviewRequestDTO;
import cl.duoc.review.Dto.ReviewResponseDTO;
import cl.duoc.review.Dto.ReviewUpdateDTO;
import cl.duoc.review.exception.InvalidReviewException;
import cl.duoc.review.exception.ReviewNotFoundException;
import cl.duoc.review.model.Review;
import cl.duoc.review.model.ReviewStatus;
import cl.duoc.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final DestinationClient destinationClient;

    public ReviewResponseDTO createReview(ReviewRequestDTO request) {

        logger.info("Creando reseña para usuario {} y destino {}", request.getUserId(), request.getDestinationId());

        validateDestination(request.getDestinationId());

        Review review = Review.builder()
                .userId(request.getUserId())
                .destinationId(request.getDestinationId())
                .rating(request.getRating())
                .comment(request.getComment())
                .status(ReviewStatus.ACTIVE)
                .build();

        Review savedReview = reviewRepository.save(review);

        logger.info("Reseña {} creada correctamente", savedReview.getId());

        return toResponseDTO(savedReview);
    }

    public List<ReviewResponseDTO> findAll() {

        logger.info("Listando reseñas activas");

        return reviewRepository.findByStatus(ReviewStatus.ACTIVE)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReviewResponseDTO findById(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Reseña no encontrada"));

        if (review.getStatus() == ReviewStatus.DELETED) {
            throw new ReviewNotFoundException("Reseña no encontrada");
        }

        return toResponseDTO(review);
    }

    public List<ReviewResponseDTO> findByUserId(Long userId) {

        logger.info("Buscando reseñas del usuario {}", userId);

        return reviewRepository.findByUserIdAndStatus(userId, ReviewStatus.ACTIVE)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ReviewResponseDTO> findByDestinationId(Long destinationId) {

        logger.info("Buscando reseñas del destino {}", destinationId);

        validateDestination(destinationId);

        return reviewRepository.findByDestinationIdAndStatus(destinationId, ReviewStatus.ACTIVE)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReviewResponseDTO updateReview(Long id, ReviewUpdateDTO request) {

        logger.info("Actualizando reseña {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Reseña no encontrada"));

        if (review.getStatus() == ReviewStatus.DELETED) {
            throw new ReviewNotFoundException("Reseña no encontrada");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        return toResponseDTO(updatedReview);
    }

    public void deleteReview(Long id) {

        logger.info("Eliminando reseña {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Reseña no encontrada"));

        if (review.getStatus() == ReviewStatus.DELETED) {
            throw new ReviewNotFoundException("Reseña no encontrada");
        }

        review.setStatus(ReviewStatus.DELETED);

        reviewRepository.save(review);
    }

    private void validateDestination(Long destinationId) {

        if (destinationId == null || destinationId <= 0) {
            throw new InvalidReviewException("El destino es invalido");
        }

        DestinationResponseDTO destination = destinationClient.getDestinationById(destinationId);

        if (destination == null || destination.getId() == null) {
            throw new InvalidReviewException("El destino no existe");
        }

        if (destination.getActive() != null && !destination.getActive()) {
            throw new InvalidReviewException("El destino no se encuentra activo");
        }
    }

    private ReviewResponseDTO toResponseDTO(Review review) {

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .destinationId(review.getDestinationId())
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
