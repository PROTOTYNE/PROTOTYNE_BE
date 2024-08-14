package com.prototyne.repository;

import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.web.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdditionalRepository extends JpaRepository<Additional, Long> {
    List<Additional> findByUserId(Long userId);
    void deleteByUserId(Long userId);

    Optional<Additional> findByUserAndAddSet_Title(User user, AddsetTitle title);
}
