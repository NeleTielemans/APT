package fact.it.person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonApplication {


	public static void main(String[] args) {

		System.out.println("🔍 About to start Spring Boot application...");

		SpringApplication.run(PersonApplication.class, args);
	}

}
