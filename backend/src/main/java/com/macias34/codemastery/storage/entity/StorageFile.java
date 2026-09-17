package com.macias34.codemastery.storage.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StorageFile {

	private String src;
	@Column(name = "file_name")
	private String fileName;

	@Column(name = "object_name")
	private String objectName;

}
