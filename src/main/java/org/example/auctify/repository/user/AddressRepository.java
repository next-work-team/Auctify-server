package org.example.auctify.repository.user;

import org.example.auctify.entity.user.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
