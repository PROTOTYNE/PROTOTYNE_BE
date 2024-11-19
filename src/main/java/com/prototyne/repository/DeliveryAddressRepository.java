package com.prototyne.repository;

import com.prototyne.domain.User;
import com.prototyne.domain.mapping.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    List<DeliveryAddress> findByUser(User user);
    Optional<DeliveryAddress> findByUserAndId(User user, Long id);
}
