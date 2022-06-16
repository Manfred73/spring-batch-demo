package nl.craftsmen.exceptionhandling;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.http.HttpStatus;

class BatchExceptionHandlerTest {

	private BatchExceptionHandler cut = new BatchExceptionHandler();

	@Test
	@DisplayName("Given a BatchjobException, I expect that an InternalServerError with a clear error message is being "
			+ "returned")
	void handleBatchjobException() {
		// GIVEN
		final var exception = new JobExecutionAlreadyRunningException("Job is already running");
		final var message = "Job with filename=file.txt is already running.";
		final var batchjobException = new BatchjobException(message, exception);

		// WHEN
		final var response = cut.handleBatchjobException(batchjobException);

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(message);
		assertThat(apiError.getValidationMessages()).isEmpty();
	}
}