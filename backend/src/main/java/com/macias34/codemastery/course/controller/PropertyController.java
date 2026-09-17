package com.macias34.codemastery.course.controller;

import com.macias34.codemastery.course.dto.category.CategoryDto;
import com.macias34.codemastery.course.dto.category.CategoryRequestDto;
import com.macias34.codemastery.course.dto.property.CreatePropertyDto;
import com.macias34.codemastery.course.dto.property.PropertyDto;
import com.macias34.codemastery.course.dto.property.UpdatePropertyDto;
import com.macias34.codemastery.course.service.PropertyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "property")
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping("")
    public ResponseEntity<PropertyDto> createProperty(
            @RequestBody CreatePropertyDto dto) {
        return ResponseEntity.ok(propertyService.createProperty(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PropertyDto> createProperty(
            @RequestBody UpdatePropertyDto dto,
            @PathVariable Integer id) {
        return ResponseEntity.ok(propertyService.updateProperty(id, dto));
    }

    @PatchMapping("/course/{id}")
    public ResponseEntity<List<PropertyDto>> overrideProperties(
            @RequestBody List<UpdatePropertyDto> dto,
            @PathVariable Integer id) {
        return ResponseEntity.ok(propertyService.overrideCourseProperties(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PropertyDto> deleteProperty(
            @PathVariable Integer id) {
        return ResponseEntity.ok(propertyService.deleteProperty(id));
    }
}
