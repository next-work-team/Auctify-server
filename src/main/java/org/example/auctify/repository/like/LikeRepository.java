package org.example.auctify.repository.like;

import org.example.auctify.entity.like.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
}
