package nl.craftsmen.exceptionhandling;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * When using WebFlux, use WebExchangeBindException instead of MissingServletRequestParameterException, since the
 * latter depends on javax.servlet.ServletException (transitive dependency from spring-web) which is not included with
 * the WebFlux dependency.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	private static final String INTERNAL_SERVER_ERROR_MESSAGE = "A generic error has occurred";
	private static final String CONSTRAINT_VIOLATION_ERROR_MESSAGE = "ConstraintViolationException occurred while processing";
	private static final String METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE =
			"MethodArgumentNotValidException occurred while processing";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException exception) {
		log.error(METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE, exception);
		return ResponseEntity.badRequest().body(getMethodArgumentNotValidExceptionApiError(exception));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleValidationException(ConstraintViolationException exception) {
		log.error(CONSTRAINT_VIOLATION_ERROR_MESSAGE, exception);
		return ResponseEntity.badRequest().body(getConstraintViolationApiError(exception));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handelException(MethodArgumentTypeMismatchException exception) {
		log.error(getMessageForException(exception), exception);
		return ResponseEntity.badRequest().body(getMethodArgumentTypeMismatchExceptionApiError(exception));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<ApiError> handelException(WebExchangeBindException exception) {
		log.error(exception.getMessage());
		return ResponseEntity.badRequest().body(getWebExchangeBindExceptionApiError(exception));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handelException(Exception exception) {
		log.error(INTERNAL_SERVER_ERROR_MESSAGE, exception);
		return ResponseEntity.internalServerError().body(getInternalServerErrorApiError());
	}

	private ApiError getMethodArgumentNotValidExceptionApiError(MethodArgumentNotValidException exception) {
		return ApiError.builder()
				.errorMessage(METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE)
				.validationMessages(getViolations(exception))
				.build();
	}

	private List<String> getViolations(MethodArgumentNotValidException exception) {
		return exception.getBindingResult().getFieldErrors().stream()
				.map(this::getMessageForViolation)
				.collect(Collectors.toUnmodifiableList());
	}

	private String getMessageForViolation(FieldError fieldError) {
		return String.format("Field %s %s", fieldError.getField(), fieldError.getDefaultMessage());
	}

	private ApiError getConstraintViolationApiError(ConstraintViolationException exception) {
		return ApiError.builder()
				.errorMessage(CONSTRAINT_VIOLATION_ERROR_MESSAGE)
				.validationMessages(getViolations(exception))
				.build();
	}

	private List<String> getViolations(ConstraintViolationException exception) {
		return exception.getConstraintViolations().stream()
				.map(this::getMessageForViolation)
				.collect(Collectors.toUnmodifiableList());
	}

	private String getMessageForViolation(ConstraintViolation<?> violation) {
		return String.format("Field %s %s", violation.getPropertyPath(), violation.getMessage());
	}

	private ApiError getMethodArgumentTypeMismatchExceptionApiError(MethodArgumentTypeMismatchException exception) {
		return ApiError.builder()
				.errorMessage(getMessageForException(exception))
				.build();
	}

	private ApiError getWebExchangeBindExceptionApiError(WebExchangeBindException exception) {
		return ApiError.builder()
				.errorMessage(exception.getMessage())
				.build();
	}

	private ApiError getInternalServerErrorApiError() {
		return ApiError.builder()
				.errorMessage(INTERNAL_SERVER_ERROR_MESSAGE)
				.build();
	}

	private String getMessageForException(MethodArgumentTypeMismatchException exception) {
		final var parameter = exception.getParameter();
		return String.format("Parameter %s should be of type %s", parameter.getParameterName(),
				parameter.getParameterType().getCanonicalName());
	}
}
