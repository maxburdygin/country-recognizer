package com.neo.country_recognizer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryPhoneCode that = (CountryPhoneCode) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(code, that.code) &&
                Objects.equals(additionalCode, that.additionalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, code, additionalCode);
    }
}
