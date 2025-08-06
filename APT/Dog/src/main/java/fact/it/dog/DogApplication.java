package fact.it.dog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class DogApplication {

    public static void main(String[] args) {


        SpringApplication.run(DogApplication.class, args);
        ConfigurableEnvironment env = new StandardEnvironment();

        System.out.println("🔍 About to start Spring Boot application...");
        System.out.println("🔌 JDBC URL: " + env.getProperty("Test123"));
        System.out.println("👤 Username: " + env.getProperty("spring.datasource.username"));
        System.out.println("🔒 Password: " +
                (env.getProperty("spring.datasource.password") != null ? "SET" : "NOT SET"));

        System.out.println(System.getenv("Test123")); }

}
