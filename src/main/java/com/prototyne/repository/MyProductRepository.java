package com.prototyne.repository;

import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyProductRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUserId(Long userId);
}
