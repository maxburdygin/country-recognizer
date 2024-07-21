package com.neo.country_recognizer.repository;

import com.neo.country_recognizer.model.CountryPhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryPhoneCodeRepository extends JpaRepository<CountryPhoneCode, Long> {
    List<CountryPhoneCode> findByCode(String code);
}

