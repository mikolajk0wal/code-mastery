package com.macias34.codemastery.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macias34.codemastery.course.entity.ThumbnailEntity;

public interface ThumbnailRepository extends JpaRepository<ThumbnailEntity, Integer> {

}
