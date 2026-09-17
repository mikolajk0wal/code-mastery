package com.macias34.codemastery.course.service;

import com.macias34.codemastery.course.dto.property.CreatePropertyDto;
import com.macias34.codemastery.course.dto.property.PropertyDto;
import com.macias34.codemastery.course.dto.property.UpdatePropertyDto;
import com.macias34.codemastery.course.entity.CourseEntity;
import com.macias34.codemastery.course.entity.PropertyEntity;
import com.macias34.codemastery.course.mapper.PropertyMapper;
import com.macias34.codemastery.course.repository.CourseRepository;
import com.macias34.codemastery.course.repository.PropertyRepository;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.util.DtoValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyService {
    private PropertyRepository propertyRepository;
    private CourseRepository courseRepository;
    private PropertyMapper propertyMapper;

    @Transactional
    public PropertyDto createProperty(CreatePropertyDto dto){
        DtoValidator.validate(dto);

        CourseEntity course = courseRepository.findById(dto.getCourseId()).orElseThrow(()-> new ResourceNotFoundException("Course not found"));

        PropertyEntity property = new PropertyEntity(dto.getLabel(),dto.getValue(),course);
        Set<PropertyEntity> properties = course.getProperties();
        properties.add(property);
        course.setProperties(properties);

        propertyRepository.save(property);
        courseRepository.save(course);

        return propertyMapper.fromEntityToDto(property);
    }

    @Transactional
    public PropertyDto deleteProperty(int id){
        PropertyEntity property = propertyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Property not found"));

        propertyRepository.deleteById(id);

        return propertyMapper.fromEntityToDto(property);
    }

    @Transactional
    public PropertyDto updateProperty(int id, UpdatePropertyDto dto){
        DtoValidator.validate(dto);

        PropertyEntity property = propertyRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Property not found"));

        if(dto.getLabel() != null){
            property.setLabel(dto.getLabel());
        }

        if(dto.getValue() != null){
            property.setValue(dto.getValue());
        }

        return propertyMapper.fromEntityToDto(property);
    }

    @Transactional
    public List<PropertyDto> overrideCourseProperties(int id, List<UpdatePropertyDto> dtos){
        for(UpdatePropertyDto dto: dtos){
            DtoValidator.validate(dto);
        }
        CourseEntity courseEntity = courseRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        System.out.println(courseEntity.getProperties().size());
        propertyRepository.deleteAll(courseEntity.getProperties());
        List<PropertyEntity> propertiesToSave = new ArrayList<>();
        for(UpdatePropertyDto dto: dtos){
            PropertyEntity property = new PropertyEntity(dto.getLabel(),dto.getValue(),courseEntity);
            propertiesToSave.add(property);
        }

        courseEntity.setProperties(new HashSet<>(propertiesToSave));
        propertyRepository.saveAll(propertiesToSave);
        courseRepository.save(courseEntity);

        return propertiesToSave.stream().map(propertyMapper::fromEntityToDto).toList();
    }
}
