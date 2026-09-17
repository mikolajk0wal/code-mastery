package com.macias34.codemastery.informationPage.controller;

import com.macias34.codemastery.informationPage.dto.CreateInformationPageDto;
import com.macias34.codemastery.informationPage.dto.InformationPageDto;
import com.macias34.codemastery.informationPage.dto.UpdateInformationPageDto;
import com.macias34.codemastery.informationPage.service.InformationPageService;

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
@RequestMapping("/information-page")
@Tag(name = "information page")
@RequiredArgsConstructor
public class InformationPageController {
    private final InformationPageService informationPageService;

    // TODO: FIX SLUGING SYSTEM
    @GetMapping()
    public ResponseEntity<List<InformationPageDto>> getAllInformationPages() {
        return ResponseEntity.ok(informationPageService.getAllInformationPages());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<InformationPageDto> getInformationPagesBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(informationPageService.getInformationPageBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<InformationPageDto> createInformationPage(
            @RequestBody CreateInformationPageDto dto) {
        return ResponseEntity.ok(informationPageService.createInformationPage(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InformationPageDto> editInformationPage(
            @RequestBody UpdateInformationPageDto dto,
            @PathVariable int id) {
        return ResponseEntity.ok(informationPageService.updateInformationPage(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InformationPageDto> deleteInformationPage(
            @PathVariable int id) {
        return ResponseEntity.ok(informationPageService.deleteInformationPageById(id));
    }
}
