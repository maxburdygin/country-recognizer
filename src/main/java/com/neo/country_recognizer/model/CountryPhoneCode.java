package com.neo.country_recognizer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "country_phone_code")
@NoArgsConstructor
public class CountryPhoneCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String code;
    private String additionalCode;
}
