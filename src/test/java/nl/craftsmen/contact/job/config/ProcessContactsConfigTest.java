package nl.craftsmen.contact.job.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProcessContactsConfigTest {

	@Test
	@DisplayName("Given a chunk size of 20, when creating the properties and calling chunkSize, I expect 10 to be " +
			"returned")
	void chunkSizeExpectToBe10() {
		// GIVEN
		final var chunkSize = 10;
		final var props = ProcessContactsConfigProps.builder().chunkSize(chunkSize).build();

		// WHEN
		final var result = new ProcessContactsConfig().chunkSize(props);

		// THEN
		assertThat(result).isEqualTo(chunkSize);
	}
}