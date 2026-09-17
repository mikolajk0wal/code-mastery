package com.macias34.codemastery.course.entity;

import com.macias34.codemastery.storage.entity.StorageFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "thumbnail")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ThumbnailEntity extends StorageFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String src;
	@Column(name = "file_name")
	private String fileName;

	@Column(name = "object_name")
	private String objectName;

	@OneToOne(mappedBy = "thumbnail")
	private CourseEntity course;

	public ThumbnailEntity(String src, String fileName, String objectName, CourseEntity course) {
		this.src = src;
		this.fileName = fileName;
		this.objectName = objectName;
		this.course = course;
	}

}
