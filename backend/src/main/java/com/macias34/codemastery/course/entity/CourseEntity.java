package com.macias34.codemastery.course.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.macias34.codemastery.order.entity.OrderEntity;
import com.macias34.codemastery.storage.entity.StorageFile;
import com.macias34.codemastery.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private double price;
    @Column(name = "instructor_name")
    private String instructorName;
    @Column(name = "participants_count")
    private int participantsCount;
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private ThumbnailEntity thumbnail;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_category", joinColumns = { @JoinColumn(name = "course_id") }, inverseJoinColumns = {
            @JoinColumn(name = "category_id") })
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PropertyEntity> properties = new HashSet<>();

    // @JsonBackReference
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterEntity> chapters;

    // @JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    private List<OrderEntity> orders;

    public CourseEntity(String name, double price, String instructorName, int participantsCount, String description) {
        this.name = name;
        this.price = price;
        this.instructorName = instructorName;
        this.participantsCount = participantsCount;
        this.description = description;
    }

}
