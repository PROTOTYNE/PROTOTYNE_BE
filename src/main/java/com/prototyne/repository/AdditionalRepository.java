package com.prototyne.repository;

import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdditionalRepository extends JpaRepository<Additional, Long> {
    List<Additional> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    Optional<Additional> findByUserAndAddSet_Title(User user, AddsetTitle title);

    void deleteByUserAndAddSet_Title(User user, AddsetTitle title);
}
