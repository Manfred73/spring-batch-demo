package nl.craftsmen.contact.job.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import java.util.List;
import nl.craftsmen.contact.model.ContactWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NoOpItemWriterTest {

	private NoOpItemWriter cut = new NoOpItemWriter();

	@Test
	@DisplayName("When write is being called with a list of ContactWrapper, I expect no exception")
	void write() {
		// GIVEN
		final var list = List.of(ContactWrapper.builder().build());

		// WHEN / THEN
		assertThatNoException().isThrownBy(() -> cut.write(list));
	}

	@Test
	@DisplayName("When write is being called without a list, I expect a NullPointerException")
	void handleLineWithoutStringExpectNullPointerException() {
		// GIVEN / WHEN
		final var thrown = catchThrowable(() -> cut.write(null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("list is marked non-null but is null");
	}
}