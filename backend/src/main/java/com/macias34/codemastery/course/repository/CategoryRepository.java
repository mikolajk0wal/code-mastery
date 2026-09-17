package com.macias34.codemastery.course.repository;

import com.macias34.codemastery.course.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Boolean existsByName(String name);
}
