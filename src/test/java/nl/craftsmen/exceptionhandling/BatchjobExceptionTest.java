package nl.craftsmen.exceptionhandling;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchjobExceptionTest {

	@Mock
	private BatchjobException batchjobExceptionMock;

	@Test
	@DisplayName("Given an exception, I expect a BatchjobException with a message and cause to be returned")
	void expectABatchjobExceptionWithAMessageAndCause() {
		// GIVEN / WHEN
		final var exception = new BatchjobException("MSG", batchjobExceptionMock);
		final var expectedMessage = "MSG";

		// THEN
		assertThat(exception)
				.extracting(
						Throwable::getMessage,
						Throwable::getCause)
				.containsExactly(
						expectedMessage,
						batchjobExceptionMock);
	}
}