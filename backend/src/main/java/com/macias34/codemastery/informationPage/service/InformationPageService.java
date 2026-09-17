package com.macias34.codemastery.informationPage.service;

import com.macias34.codemastery.course.repository.CategoryRepository;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.informationPage.dto.CreateInformationPageDto;
import com.macias34.codemastery.informationPage.dto.InformationPageDto;
import com.macias34.codemastery.informationPage.dto.UpdateInformationPageDto;
import com.macias34.codemastery.informationPage.entity.InformationPageEntity;
import com.macias34.codemastery.informationPage.mapper.InformationPageMapper;
import com.macias34.codemastery.informationPage.repository.InformationPageRepository;
import com.macias34.codemastery.util.DtoValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@AllArgsConstructor
public class InformationPageService {
    private final InformationPageRepository informationPageRepository;
    private final InformationPageMapper informationPageMapper;

    public InformationPageDto createInformationPage(CreateInformationPageDto dto){
        DtoValidator.validate(dto);
        String slug = generateSlug(dto.getTitle(),null);
        InformationPageEntity informationPage = new InformationPageEntity(dto.getTitle(),slug,dto.getContent());
        informationPageRepository.save(informationPage);

        return informationPageMapper.fromEntityToDto(informationPage);
    }

    public InformationPageDto getInformationPageBySlug(String slug){
        InformationPageEntity informationPage = informationPageRepository.findInformationPageEntityBySlug(slug).orElseThrow(()->new ResourceNotFoundException("Information page not found"));

        return informationPageMapper.fromEntityToDto(informationPage);
    }

    public List<InformationPageDto> getAllInformationPages(){
        List<InformationPageEntity> informationPages = informationPageRepository.findAll();

        if(informationPages.isEmpty()){
            throw new ResourceNotFoundException("Information pages not found");
        }

        return informationPages.stream().map(informationPageMapper::fromEntityToDto).toList();
    }

    public InformationPageDto updateInformationPage(int id, UpdateInformationPageDto dto){
        DtoValidator.validate(dto);

        InformationPageEntity informationPage = informationPageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Information page not found"));

        if(dto.getTitle()!=null){
            String slug = generateSlug(dto.getTitle(),id);
            informationPage.setSlug(slug);
            informationPage.setTitle(dto.getTitle());
        }
        if(dto.getContent() != null){
            informationPage.setContent(dto.getContent());
        }

        informationPageRepository.save(informationPage);

        return informationPageMapper.fromEntityToDto(informationPage);
    }

    public InformationPageDto deleteInformationPageById(int id){
        InformationPageEntity informationPage = informationPageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Information page not found"));

        informationPageRepository.deleteById(id);

        return informationPageMapper.fromEntityToDto(informationPage);
    }

    private String generateSlug(String title, Integer updatingPageId){
        List<InformationPageEntity> informationPages = informationPageRepository.findInformationPageEntitiesByTitleIgnoreCase(title);
        int count = informationPages.stream().filter(i -> i.getId() != updatingPageId).toList().size();
        String lowercase = title.toLowerCase();
        String normalized = Normalizer.normalize(lowercase, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\s+", "-");
        slug = slug.replaceAll("[^a-zA-Z0-9-]", "");
        if(count > 0){
            slug = slug + "-" + count;
        }
        return slug;
    }

}
