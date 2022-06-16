package nl.craftsmen.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class BatchExceptionHandler {

	@ExceptionHandler(BatchjobException.class)
	public ResponseEntity<ApiError> handleBatchjobException(BatchjobException exception) {
		log.error(exception.getMessage(), exception);
		return ResponseEntity.internalServerError().body(getBatchjobExceptionApiError(exception));
	}

	private ApiError getBatchjobExceptionApiError(BatchjobException exception) {
		return ApiError.builder()
				.errorMessage(exception.getMessage())
				.build();
	}
}
