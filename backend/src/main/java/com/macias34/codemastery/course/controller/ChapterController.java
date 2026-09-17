package com.macias34.codemastery.course.controller;

import com.macias34.codemastery.course.dto.chapter.ChapterDto;
import com.macias34.codemastery.course.dto.chapter.CreateChapterDto;
import com.macias34.codemastery.course.dto.chapter.UpdateChapterDto;
import com.macias34.codemastery.course.dto.lesson.LessonDto;
import com.macias34.codemastery.course.service.ChapterService;

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
@RequestMapping("/chapter")
@Tag(name = "chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapterById(
            @PathVariable int id) {
        return ResponseEntity.ok(chapterService.getChapterById(id));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(
            @PathVariable int id) {
        return ResponseEntity.ok(chapterService.getChaptersByCourseId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChapterDto> deleteChapterById(
            @PathVariable int id) {
        return ResponseEntity.ok(chapterService.deleteChapterById(id));
    }

    @PostMapping("")
    public ResponseEntity<ChapterDto> createChapter(
            @RequestBody CreateChapterDto dto) {
        return ResponseEntity.ok(chapterService.createChapter(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChapterDto> updateChapter(
            @RequestBody UpdateChapterDto dto,
            @PathVariable int id) {
        return ResponseEntity.ok(chapterService.updateChapter(id, dto));
    }
}
