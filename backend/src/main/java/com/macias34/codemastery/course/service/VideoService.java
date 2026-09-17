package com.macias34.codemastery.course.service;

import org.springframework.stereotype.Service;

import com.macias34.codemastery.course.entity.LessonEntity;
import com.macias34.codemastery.course.entity.VideoEntity;
import com.macias34.codemastery.course.repository.VideoRepository;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.storage.service.StorageService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VideoService {
	private final VideoRepository videoRepository;
	private final StorageService storageService;

	public void deleteLessonVideoIfExists(VideoEntity previousVideo) {
		if (previousVideo != null) {
			videoRepository.delete(previousVideo);
			storageService.deleteFile(previousVideo.getObjectName());
		}
	}

	private VideoEntity getVideoEntityById(int id) {
		VideoEntity video = videoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Video not found"));

		return video;
	}

}
