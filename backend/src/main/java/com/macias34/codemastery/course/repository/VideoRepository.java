package com.macias34.codemastery.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.macias34.codemastery.course.entity.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, Integer> {

}
