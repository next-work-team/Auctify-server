package org.example.auctify.repository.user;

import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByOauthId(String oauthId);

    @Query("select u from UserEntity u left join fetch u.receivedReviews where u.userId = :userId")
    Optional<UserEntity> findByIdWithReceivedReviews(@Param("userId") Long userId);

    @Query("select u from UserEntity u left join fetch u.address where u.userId = :userId")
    Optional<UserEntity> findByIdWithAddresses(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.nickName = :nickname AND u.userId <> :userId")
    long countByNickNameExcludingUserId(@Param("nickname") String nickname, @Param("userId") Long userId);
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email AND u.userId <> :userId")
    long countByEmailExcludingUserId(@Param("email") String email, @Param("userId") Long userId);

    long countByNickName(String nickname);

    long countByEmail(String email);


}
