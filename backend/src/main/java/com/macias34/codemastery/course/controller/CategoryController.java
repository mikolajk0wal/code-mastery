package com.macias34.codemastery.course.controller;

import com.macias34.codemastery.course.dto.category.CategoryDto;
import com.macias34.codemastery.course.dto.category.CategoryRequestDto;
import com.macias34.codemastery.course.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "category")
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable int id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryRequestDto dto,
            @PathVariable int id) {
        return ResponseEntity.ok(categoryService.updateDto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> deleteCategoryById(
            @PathVariable int id) {
        return ResponseEntity.ok(categoryService.deleteCategoryById(id));
    }
}
