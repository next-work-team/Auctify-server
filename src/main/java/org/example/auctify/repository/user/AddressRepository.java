package org.example.auctify.repository.user;

import org.example.auctify.entity.user.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByUser_UserId(Long userId);
}
