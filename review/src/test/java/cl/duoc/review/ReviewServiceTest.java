package cl.duoc.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import cl.duoc.review.service.ReviewService;
import cl.duoc.review.client.DestinationClient;
import cl.duoc.review.Dto.DestinationResponseDTO;
import cl.duoc.review.Dto.ReviewRequestDTO;
import cl.duoc.review.Dto.ReviewResponseDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.model.ReviewStatus;
import cl.duoc.review.repository.ReviewRepository;

public class ReviewServiceTest {

    @Test
    void createReviewShouldReturnReviewResponse() {

        ReviewRepository reviewRepository = Mockito.mock(ReviewRepository.class);
        DestinationClient destinationClient = Mockito.mock(DestinationClient.class);

        ReviewService reviewService = new ReviewService(reviewRepository, destinationClient);

        ReviewRequestDTO request = new ReviewRequestDTO(
                1L,
                1L,
                5,
                "Excelente destino turistico"
        );

        DestinationResponseDTO destination = new DestinationResponseDTO(
                1L,
                "Valparaiso",
                "Chile",
                "Valparaiso",
                true
        );

        Review savedReview = Review.builder()
                .id(1L)
                .userId(1L)
                .destinationId(1L)
                .rating(5)
                .comment("Excelente destino turistico")
                .status(ReviewStatus.ACTIVE)
                .build();

        when(destinationClient.getDestinationById(1L)).thenReturn(destination);
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(savedReview);

        ReviewResponseDTO result = reviewService.createReview(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRating()).isEqualTo(5);
    }

    @Test
    void findByIdShouldReturnReview() {

        ReviewRepository reviewRepository = Mockito.mock(ReviewRepository.class);
        DestinationClient destinationClient = Mockito.mock(DestinationClient.class);

        ReviewService reviewService = new ReviewService(reviewRepository, destinationClient);

        Review review = Review.builder()
                .id(1L)
                .userId(1L)
                .destinationId(1L)
                .rating(4)
                .comment("Muy buen destino")
                .status(ReviewStatus.ACTIVE)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponseDTO result = reviewService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findAllShouldReturnActiveReviews() {

        ReviewRepository reviewRepository = Mockito.mock(ReviewRepository.class);
        DestinationClient destinationClient = Mockito.mock(DestinationClient.class);

        ReviewService reviewService = new ReviewService(reviewRepository, destinationClient);

        Review review = Review.builder()
                .id(1L)
                .userId(1L)
                .destinationId(1L)
                .rating(5)
                .comment("Excelente")
                .status(ReviewStatus.ACTIVE)
                .build();

        when(reviewRepository.findByStatus(ReviewStatus.ACTIVE)).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.findAll();

        assertThat(result).hasSize(1);
    }
}