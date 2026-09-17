package com.macias34.codemastery.user.service;

import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.user.dto.InvoiceDetailsDto;
import com.macias34.codemastery.user.dto.UpdateInvoiceDetailsDto;
import com.macias34.codemastery.user.entity.InvoiceDetailsEntity;
import com.macias34.codemastery.user.repository.InvoiceDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceDetailsService {
    private final InvoiceDetailsRepository invoiceDetailsRepository;
    @Transactional
    public InvoiceDetailsEntity updateInvoiceDetails(int id, UpdateInvoiceDetailsDto dto){
        InvoiceDetailsEntity invoiceDetails = invoiceDetailsRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Invoice details not found"));

        if(dto.getCity() != null){
            invoiceDetails.setCity(dto.getCity());
        }

        if(dto.getNip() != null){
            invoiceDetails.setNip(dto.getNip());
        }

        if(dto.getCompanyName() != null){
            invoiceDetails.setCompanyName(dto.getCompanyName());
        }

        if(dto.getFirstName() != null){
            invoiceDetails.setFirstName(dto.getFirstName());
        }

        if(dto.getLastName() != null){
            invoiceDetails.setLastName(dto.getLastName());
        }

        if(dto.getStreet() != null){
            invoiceDetails.setStreet(dto.getStreet());
        }

        if(dto.getPostalCode() != null){
            invoiceDetails.setPostalCode(dto.getPostalCode());
        }

        if(dto.getPhoneNumber() != null){
            invoiceDetails.setPhoneNumber(dto.getPhoneNumber());
        }
        invoiceDetailsRepository.save(invoiceDetails);

        return invoiceDetails;
    }
}
