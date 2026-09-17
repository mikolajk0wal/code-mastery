package com.macias34.codemastery.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personal_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "postal_code")
    private String postalCode;
    private String city;
    private String street;
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "personalDetails", fetch = FetchType.LAZY)
    private UserEntity user;

    public PersonalDetailsEntity(String firstName, String lastName, String postalCode, String city, String street, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.phoneNumber = phoneNumber;
    }
}
