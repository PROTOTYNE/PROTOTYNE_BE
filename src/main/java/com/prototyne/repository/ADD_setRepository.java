package com.prototyne.repository;

import com.prototyne.domain.ADD_set;
import com.prototyne.domain.enums.AddsetTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ADD_setRepository extends JpaRepository<ADD_set, Long> {
    Optional<ADD_set> findByTitleAndValue(AddsetTitle title, String value);
}
