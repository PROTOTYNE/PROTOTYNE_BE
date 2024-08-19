package com.prototyne.repository;

import com.prototyne.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRespository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserId(Long userId);
}
