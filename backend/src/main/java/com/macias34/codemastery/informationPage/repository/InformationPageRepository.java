package com.macias34.codemastery.informationPage.repository;

import com.macias34.codemastery.informationPage.entity.InformationPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InformationPageRepository extends JpaRepository<InformationPageEntity,Integer> {
    List<InformationPageEntity> findInformationPageEntitiesByTitleIgnoreCase(String title);
    Optional<InformationPageEntity> findInformationPageEntityBySlug(String slug);
}
