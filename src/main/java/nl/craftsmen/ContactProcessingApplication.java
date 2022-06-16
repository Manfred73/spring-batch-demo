package nl.craftsmen;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { ContactProcessingApplication.ROOT })
@EnableBatchProcessing
public class ContactProcessingApplication {

	public static final String ROOT = "nl.craftsmen";

	public static void main(String[] args) {
		SpringApplication.run(ContactProcessingApplication.class, args);
	}
}
