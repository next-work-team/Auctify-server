package org.example.auctify.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.user.UserInfoResponseDTO;
import org.example.auctify.entity.review.ReviewEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;


    public UserInfoResponseDTO getProfile(Long userId) {


        // 그냥 UserEntity를 가져와서 ReviewEntity에 접근하면 N +1 문제가 발생한다 이를 Fetch조인으로 예방함
            UserEntity user = userRepository.findByIdWithReceivedReviews(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<ReviewEntity> reviewList = user.getReceivedReviews();

        // 유저의 매너온도 기본값 0.0
        double temperature = 0.0;

        if (!reviewList.isEmpty()) {
            temperature = reviewList.stream()
                    .mapToDouble(ReviewEntity::getTemperature)
                    .average()
                    .orElse(0.0);
        }

        return UserInfoResponseDTO.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .profileImage(user.getImage())
                .birthdate(user.getBirthday() != null ? user.getBirthday().toString() : null)
                .mannerTemperature(temperature)
                .build();


    }
}
