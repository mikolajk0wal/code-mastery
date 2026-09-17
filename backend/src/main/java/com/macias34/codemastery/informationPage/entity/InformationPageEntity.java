package com.macias34.codemastery.informationPage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "information_page")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InformationPageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String slug;
    private String content;

    public InformationPageEntity(String title, String slug, String content) {
        this.title = title;
        this.slug = slug;
        this.content = content;
    }
}
