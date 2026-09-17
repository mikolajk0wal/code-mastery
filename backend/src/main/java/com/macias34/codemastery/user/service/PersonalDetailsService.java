package com.macias34.codemastery.user.service;

import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.user.dto.UpdatePersonalDetailsDto;
import com.macias34.codemastery.user.entity.PersonalDetailsEntity;
import com.macias34.codemastery.user.repository.InvoiceDetailsRepository;
import com.macias34.codemastery.user.repository.PersonalDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class PersonalDetailsService {
    private final PersonalDetailsRepository personalDetailsRepository;

    @Transactional
    public PersonalDetailsEntity updatePersonalDetails(int id, UpdatePersonalDetailsDto dto){
        PersonalDetailsEntity personalDetails = personalDetailsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Personal details not found"));

        if(dto.getCity() != null){
            personalDetails.setCity(dto.getCity());
        }

        if(dto.getFirstName() != null){
            personalDetails.setFirstName(dto.getFirstName());
        }

        if(dto.getLastName() != null){
            personalDetails.setLastName(dto.getLastName());
        }

        if(dto.getStreet() != null){
            personalDetails.setStreet(dto.getStreet());
        }

        if(dto.getPostalCode() != null){
            personalDetails.setPostalCode(dto.getPostalCode());
        }

        if(dto.getPhoneNumber() != null){
            personalDetails.setPhoneNumber(dto.getPhoneNumber());
        }

        personalDetailsRepository.save(personalDetails);

        return personalDetails;

    }

}
