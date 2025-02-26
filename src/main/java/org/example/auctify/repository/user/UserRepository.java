package org.example.auctify.repository.user;

import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByOauthId(String oauthId);
}
