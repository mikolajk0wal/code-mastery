package com.macias34.codemastery.course.repository;

import com.macias34.codemastery.course.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<PropertyEntity,Integer> {
}
