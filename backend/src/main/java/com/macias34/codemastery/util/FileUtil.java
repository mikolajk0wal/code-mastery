package com.macias34.codemastery.util;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	public static String getFileExtension(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename != null) {
			int lastIndex = originalFilename.lastIndexOf('.');
			if (lastIndex != -1) {
				return originalFilename.substring(lastIndex);
			}
		}
		return null;
	}

	public static boolean isImage(MultipartFile file) {
		return file.getContentType() != null && file.getContentType().startsWith("image");
	}

	public static boolean isVideo(MultipartFile file) {
		return file.getContentType() != null && file.getContentType().startsWith("video");
	}

}
