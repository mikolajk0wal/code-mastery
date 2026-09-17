package com.macias34.codemastery.course.service;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.course.dto.lesson.CreateLessonDto;
import com.macias34.codemastery.course.dto.lesson.LessonDto;
import com.macias34.codemastery.course.dto.lesson.UpdateLessonDto;
import com.macias34.codemastery.course.entity.ChapterEntity;
import com.macias34.codemastery.course.entity.LessonEntity;
import com.macias34.codemastery.course.entity.VideoEntity;
import com.macias34.codemastery.course.mapper.LessonMapper;
import com.macias34.codemastery.course.repository.ChapterRepository;
import com.macias34.codemastery.course.repository.LessonRepository;
import com.macias34.codemastery.exception.BadRequestException;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.exception.StorageException;
import com.macias34.codemastery.storage.entity.StorageFile;
import com.macias34.codemastery.storage.service.StorageService;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.service.UserService;
import com.macias34.codemastery.util.DtoValidator;
import com.macias34.codemastery.util.FileUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final VideoService videoService;
    private final LessonMapper lessonMapper;
    private final StorageService storageService;
    private final UserService userService;

    @Transactional
    public LessonDto createLesson(CreateLessonDto dto) {
        DtoValidator.validate(dto);

        ChapterEntity chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
        LessonEntity lesson = new LessonEntity(dto.getTitle(), chapter);

        lessonRepository.save(lesson);

        return lessonMapper.fromEntityToDto(lesson);
    }

    public void uploadLessonVideo(int id, MultipartFile file) {
        LessonEntity lesson = getLessonEntityById(id);

        if (!FileUtil.isVideo(file)) {
            throw new BadRequestException("Uploaded video isn't a video type.");
        }

        try {

            String fileExtension = FileUtil.getFileExtension(file);
            String fileName = UUID.randomUUID().toString() + fileExtension;
            String objectName = "protected/lessons/" + fileName;

            VideoEntity previousVideo = lesson.getVideo();

            StorageFile storageFile = storageService.uploadFile(fileName, objectName, file);

            VideoEntity video = new VideoEntity(storageFile.getSrc(), storageFile.getFileName(),
                    storageFile.getObjectName(), lesson);

            lesson.setVideo(video);
            videoService.deleteLessonVideoIfExists(previousVideo);

        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                System.out.println(e);
            }
            throw new StorageException("Error with saving file occured");
        }

        lessonRepository.save(lesson);

    }

    public LessonDto deleteLessonById(int id) {
        LessonEntity lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        try {
            storageService.deleteFile(lesson.getId() + ".mp4");
            lessonRepository.deleteById(lesson.getId());
        } catch (Exception e) {
            throw new StorageException("Error with removing file occurred");
        }

        return lessonMapper.fromEntityToDto(lesson);
    }

    public LessonDto updateLessonById(int id, UpdateLessonDto dto) {
        DtoValidator.validate(dto);

        LessonEntity lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lesson.setTitle(dto.getTitle());
        lessonRepository.save(lesson);

        return lessonMapper.fromEntityToDto(lesson);
    }

    public LessonDto getLessonById(int id) {
        LessonEntity lesson = getLessonEntityById(id);
        LessonDto lessonDto = lessonMapper.fromEntityToDto(lesson);
        VideoEntity video = lesson.getVideo();

        if (video != null) {

            User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();

            UserDto user = userService.getUserByUsername(sessionUser.getUsername());
            boolean isUserAuthorizedForCourse = checkIfUserIsAuthorizedForCourse(user, lesson);
            if (isUserAuthorizedForCourse) {
                String videoPresignedUrl = storageService.generatePresignedUrl(video.getObjectName(), 30).toString();
                lessonDto.setVideoSrc(videoPresignedUrl);
            }

        }

        return lessonDto;
    }

    private boolean checkIfUserIsAuthorizedForCourse(UserDto user, LessonEntity lesson) {
        boolean isUserAuthorizedForCourse = false;
        if (user.getRole().equals(UserRole.ADMIN)) {
            isUserAuthorizedForCourse = true;
        }

        else {
            for (CourseDto course : user.getCourses()) {
                if (course.getId() == lesson.getChapter().getCourse().getId()) {
                    isUserAuthorizedForCourse = true;
                    break;
                }
            }
        }

        return isUserAuthorizedForCourse;
    }

    private LessonEntity getLessonEntityById(int id) {
        LessonEntity lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        return lesson;
    }

    public List<LessonDto> getLessonsByChapterId(int chapterId) {
        List<LessonEntity> lessons = lessonRepository.findLessonEntitiesByChapter_Id(chapterId);
        if (lessons.isEmpty()) {
            throw new ResourceNotFoundException("Lessons not found");
        }
        return lessons.stream().map(lessonMapper::fromEntityToDto).toList();
    }

}
