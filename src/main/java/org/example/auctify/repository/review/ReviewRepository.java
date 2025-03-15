package org.example.auctify.repository.review;

import org.example.auctify.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {
}
