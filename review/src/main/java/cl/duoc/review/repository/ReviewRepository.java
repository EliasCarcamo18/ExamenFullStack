package cl.duoc.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.review.model.Review;
import cl.duoc.review.model.ReviewStatus;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    List<Review> findByDestinationId(Long destinationId);

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByDestinationIdAndStatus(Long destinationId, ReviewStatus status);

    List<Review> findByUserIdAndStatus(Long userId, ReviewStatus status);
}