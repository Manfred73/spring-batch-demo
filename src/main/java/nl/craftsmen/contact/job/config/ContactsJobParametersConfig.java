package nl.craftsmen.contact.job.config;

import java.util.Date;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactsJobParametersConfig {

	private static final String DATE_CONTEXT_KEY = "date";
	private static final String FILE_NAME_CONTEXT_KEY = "filename";

	/**
	 * For demo purposes this is now a static file from src/main/resources.
	 */
	private static final String FILE_TO_PROCESS = "contacts.txt";

	@Bean
	public JobParameters getJobParameters() {
		final var jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString(FILE_NAME_CONTEXT_KEY, FILE_TO_PROCESS);
		jobParametersBuilder.addDate(DATE_CONTEXT_KEY, new Date());
		return jobParametersBuilder.toJobParameters();
	}
}
