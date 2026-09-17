package com.macias34.codemastery.user.repository;

import com.macias34.codemastery.user.entity.PersonalDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetailsEntity, Integer> {
}
