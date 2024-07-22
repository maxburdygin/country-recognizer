package com.neo.country_recognizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CountryRecognizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountryRecognizerApplication.class, args);
	}
}
