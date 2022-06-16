package nl.craftsmen.contact.job.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContactsJobParametersConfigTest {

	@Test
	@DisplayName("When calling getJobParameters, I expect job parameters to be returned")
	void getJobParametersVerwachtJobParametersTerugTeKrijgen() {
		// GIVEN
		final var contactsJobParametersConfig = new ContactsJobParametersConfig();

		// WHEN
		final var jobParameters = contactsJobParametersConfig.getJobParameters();

		// THEN
		assertThat(jobParameters)
				.extracting(
						jp -> jp.getParameters().get("filename"),
						jp -> jp.getParameters().get("date"))
				.doesNotContainNull();
	}
}