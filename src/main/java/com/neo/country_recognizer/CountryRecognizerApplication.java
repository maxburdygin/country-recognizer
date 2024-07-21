package com.neo.country_recognizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CountryRecognizerApplication {

	//TODO 8. START from terminal
	//TODO 14. TESTS for job
	//TODO 16. TESTS for uploading from Wiki page
	//TODO 15. Run TESTS from terminal

	public static void main(String[] args) {
		SpringApplication.run(CountryRecognizerApplication.class, args);
	}
}
