package com.macias34.codemastery.course.controller;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.course.dto.course.CourseResponseDto;
import com.macias34.codemastery.course.dto.course.UpdateCourseDto;
import com.macias34.codemastery.course.model.CourseFilter;
import com.macias34.codemastery.course.service.CourseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@Tag(name = "course")
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;

    @GetMapping("")
    public ResponseEntity<CourseResponseDto> searchCourses(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Double minPrice,
            @RequestParam(defaultValue = "100000") Double maxPrice,
            @RequestParam(defaultValue = "0") Integer minParticipantsCount,
            @RequestParam(defaultValue = "0") Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        CourseFilter courseFilter = new CourseFilter(name, minPrice, maxPrice, minParticipantsCount, categoryId);

        return ResponseEntity.ok(courseService.searchCourses(courseFilter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(
            @PathVariable int id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDto> deleteCourseById(
            @PathVariable int id) {
        return ResponseEntity.ok(courseService.deleteCourseById(id));
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<CourseDto> createCourse() {

        return ResponseEntity.ok(courseService.createCourse());
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable int id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "instructorName", required = false) String instructorName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categoriesIds", required = false) Set<Integer> categoriesIds,
            @RequestParam(value = "thumbnailSrc", required = false) String thumbnailSrc,
            @RequestParam(value = "thumbnailImage", required = false) MultipartFile thumbnailImage) {
        UpdateCourseDto updateCourseDto = new UpdateCourseDto(name, price, instructorName, description, categoriesIds,
                thumbnailSrc);

        return ResponseEntity.ok(courseService.updateCourse(id, updateCourseDto, thumbnailImage));
    }

}
