package org.project.multilogin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MultiLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiLoginApplication.class, args);
	}

}
