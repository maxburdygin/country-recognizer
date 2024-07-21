package com.neo.country_recognizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CountryRecognizerApplication {

	//DONE 1. get a list of countries
	//DONE 1.1 table - code + add code = unique index
	//DONE 2. put it in a table
	//DONE 3. go by scheduler to wiki
	//DONE 4. implement cash
	//DONE 5. whenever smth is updated - update a cash
	//DONE 6. FRONTEND
	//DONE 7. TESTS
	//TODO 8. START from terminal
	//TODO 9. Logging
	//DONE 10. validate number on frontend
	//TODO 11. Tests for scheduler
	//TODO 12. Front - if unknown code
	//TODO 13. Check transactional
	//TODO 14. TESTS for job
	//TODO 15. TESTS for cache

	public static void main(String[] args) {
		SpringApplication.run(CountryRecognizerApplication.class, args);
	}



	//todo tests:
	//CountryPhoneCode(id=70985, country=Sierra Leone, code=232, additionalCode=null)


	//сортируем данные в кэше - по порядку кода страны и по порядку доп кода

	/* !!!страна остается прежней!!! */
	// сменился код страны (доп кода не было)
	// сменился код страны (доп код остался прежним)
	// сменился код страны (доп код изменился)
}
