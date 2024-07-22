package com.neo.country_recognizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CountryRecognizerApplication {

	//TODO 14. TESTS for job
	//TODO return list of countries
	//TODO logs to spell into console
	//todo check scheduler in terminal

	public static void main(String[] args) {
		SpringApplication.run(CountryRecognizerApplication.class, args);
	}
}
