package nl.craftsmen.contact.job.faulthandling;

import static org.assertj.core.api.Assertions.assertThat;
import nl.craftsmen.exceptionhandling.UnexpectedTestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemSkipPolicyTest {

	@Test
	@DisplayName("When calling shouldSkip, I expect true to be returned")
	void shouldSkipExpectTrue() {
		// GIVEN / WHEN

		final var result = new ItemSkipPolicy().shouldSkip(new UnexpectedTestException(), 10);

		// THEN
		assertThat(result).isTrue();
	}
}