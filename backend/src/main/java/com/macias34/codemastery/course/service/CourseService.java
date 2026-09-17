package com.macias34.codemastery.course.service;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.course.dto.course.CourseResponseDto;
import com.macias34.codemastery.course.dto.course.CreateCourseDto;
import com.macias34.codemastery.course.dto.course.UpdateCourseDto;
import com.macias34.codemastery.course.entity.CategoryEntity;
import com.macias34.codemastery.course.entity.ChapterEntity;
import com.macias34.codemastery.course.entity.CourseEntity;
import com.macias34.codemastery.course.entity.LessonEntity;
import com.macias34.codemastery.course.entity.ThumbnailEntity;
import com.macias34.codemastery.course.mapper.CourseMapper;
import com.macias34.codemastery.course.model.CourseFilter;
import com.macias34.codemastery.course.repository.CategoryRepository;
import com.macias34.codemastery.course.repository.CourseRepository;
import com.macias34.codemastery.course.repository.ThumbnailRepository;
import com.macias34.codemastery.exception.BadRequestException;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.exception.StorageException;
import com.macias34.codemastery.storage.entity.Base64DecodedMultipartFile;
import com.macias34.codemastery.storage.entity.StorageFile;
import com.macias34.codemastery.storage.service.StorageService;
import com.macias34.codemastery.util.DateTimeUtil;
import com.macias34.codemastery.util.DtoValidator;
import com.macias34.codemastery.util.FileUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final ThumbnailRepository thumbnailRepository;
    private final StorageService storageService;
    private final CourseMapper courseMapper;

    public CourseEntity findCourseOrThrow(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public CourseResponseDto searchCourses(CourseFilter courseFilter, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        // TODO: add filters
        Page<CourseEntity> coursesPage = this.courseRepository.searchCourseEntitiesByFilters(courseFilter, paging);
        List<CourseEntity> courses = coursesPage.getContent();

        if (courses.isEmpty()) {
            throw new ResourceNotFoundException("Courses not found");
        }

        List<CourseDto> courseDtos = courses.stream().map(course -> {
            CourseDto courseDto = courseMapper.fromEntityToDto(course);
            if (course.getThumbnail() != null) {
                courseDto.setThumbnailSrc(course.getThumbnail().getSrc());
            }
            return courseDto;
        }).collect(Collectors.toList());

        return CourseResponseDto.builder().currentPage(page)
                .totalElements(coursesPage.getTotalElements())
                .totalPages(coursesPage.getTotalPages())
                .courses(courseDtos).build();
    }

    public CourseDto getCourseById(int id) {
        CourseEntity course = findCourseOrThrow(id);

        CourseDto courseDto = courseMapper.fromEntityToDto(course);
        if (course.getThumbnail() != null) {
            courseDto.setThumbnailSrc(course.getThumbnail().getSrc());
        }

        return courseDto;
    }

    @Transactional
    public CourseDto deleteCourseById(int id) {
        CourseEntity course = findCourseOrThrow(id);
        try {
            storageService.deleteFile(course.getThumbnail().getObjectName());

            for (ChapterEntity chapter : course.getChapters()) {
                for (LessonEntity lesson : chapter.getLessons()) {
                    try {
                        storageService.deleteFile(lesson.getId() + ".mp4");
                    } catch (Exception e) {
                        throw new StorageException("Error with removing file occurred");
                    }
                }
            }

            courseRepository.deleteById(id);
        } catch (Exception e) {
            throw new StorageException("Error with removing file occurred");
        }

        return courseMapper.fromEntityToDto(course);
    }

    public CourseDto createCourse() {

        CourseEntity courseEntity = CourseEntity.builder().name("Untitled course").build();

        courseRepository.save(courseEntity);

        return courseMapper.fromEntityToDto(courseEntity);
    }

    @Transactional
    public ThumbnailEntity updateCourseThumbnail(int id, MultipartFile file) {
        if (!FileUtil.isImage(file)) {
            throw new BadRequestException("Uploaded thumbnail isn't an image type.");
        }

        CourseEntity course = findCourseOrThrow(id);

        String fileExtension = FileUtil.getFileExtension(file);
        String fileName = "thumbnail-" + course.getId();
        String objectName = "public/thumbnails/" + fileName + fileExtension;
        StorageFile storageFile = storageService.uploadPublicFile(fileName, objectName, file);
        System.out.println(storageFile);

        ThumbnailEntity thumbnailEntity = new ThumbnailEntity(storageFile.getSrc(), storageFile.getFileName(),
                storageFile.getObjectName(), course);

        thumbnailRepository.save(thumbnailEntity);

        return thumbnailEntity;
    }

    @Transactional
    public CourseDto updateCourse(int id, UpdateCourseDto dto, MultipartFile thumbnailImage) {
        DtoValidator.validate(dto);

        CourseEntity course = findCourseOrThrow(id);

        Set<CategoryEntity> categories = new HashSet<>();

        if (dto.getCategoriesIds() != null) {
            for (int categoryId : dto.getCategoriesIds()) {
                Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
                category.ifPresent(categories::add);
            }
            course.setCategories(categories);
        }

        if (dto.getPrice() != null) {
            course.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getName() != null) {
            course.setName(dto.getName());
        }
        if (dto.getInstructorName() != null) {
            course.setInstructorName(dto.getInstructorName());
        }

        if (thumbnailImage != null) {
            ThumbnailEntity thumbnail = updateCourseThumbnail(id, thumbnailImage);
            course.setThumbnail(thumbnail);
        }

        courseRepository.save(course);

        CourseDto courseDto = courseMapper.fromEntityToDto(course);
        if (course.getThumbnail() != null) {
            courseDto.setThumbnailSrc(course.getThumbnail().getSrc());

        }
        return courseDto;
    }
}
