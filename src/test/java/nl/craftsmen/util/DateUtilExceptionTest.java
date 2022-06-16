package nl.craftsmen.util;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateUtilExceptionTest {

	@Mock
	private DateTimeParseException dateTimeParseExceptionMock;

	@Test
	void expectADateUtilExceptionWithAMessage() {
		// GIVEN / WHEN
		final var exception = new DateUtilException("MSG");
		final var expectedMessage = "MSG";

		// THEN
		assertThat(exception)
				.extracting(
						Throwable::getMessage,
						Throwable::getCause)
				.containsExactly(
						expectedMessage,
						null);
	}

	@Test
	void expectADateUtilExceptionWithAMessageAndCause() {
		// GIVEN / WHEN
		final var exception = new DateUtilException("MSG", dateTimeParseExceptionMock);
		final var expectedMessage = "MSG";

		// THEN
		assertThat(exception)
				.extracting(
						Throwable::getMessage,
						Throwable::getCause)
				.containsExactly(
						expectedMessage,
						dateTimeParseExceptionMock);
	}
}