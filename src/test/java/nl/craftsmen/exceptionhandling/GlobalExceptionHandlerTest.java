package nl.craftsmen.exceptionhandling;

import static nl.craftsmen.ContactsTestdataSupplier.CONSTRAINT_VIOLATION_ERROR_MESSAGE;
import static nl.craftsmen.ContactsTestdataSupplier.INTERNAL_SERVER_ERROR_MESSAGE;
import static nl.craftsmen.ContactsTestdataSupplier.METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE;
import static nl.craftsmen.ContactsTestdataSupplier.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE;
import static nl.craftsmen.ContactsTestdataSupplier.createContact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import nl.craftsmen.contact.model.Contact;
import nl.craftsmen.validation.ValidatorUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	private static final String PARAMETER_NAME = "parameterName";
	private static final String CONSTRAINT_VIOLATION_VALIDATION_MESSAGE = "Field firstName must not be null";
	private static final String METHOD_ARGUMENT_NOT_VALID_VALIDATION_MESSAGE = "Field firstName cannot be null";

	private final GlobalExceptionHandler cut = new GlobalExceptionHandler();

	private static Validator validator;

	@Mock
	private MethodArgumentNotValidException methodArgumentNotValidException;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private MethodParameter methodParameter;

	@Mock
	private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

	@Mock
	private WebExchangeBindException webExchangeBindExceptionMock;

	@BeforeAll
	static void setup() {
		validator = ValidatorUtil.getValidator();
	}

	@Test
	@DisplayName("When a MethodArgumentNotValidException occurs, I expect a Bad Request response with an ApiError "
			+ "and an error message and 1 validation message")
	void handleValidationExceptionMethodArgumentNotValidExceptionExpectResponse400AndApiErrorWithErrorMessageAndValidatieMessage() {
		// GIVEN
		final var methodArgumentNotValidExceptionMock = createMethodArgumentNotValidExceptionMock();

		// WHEN
		final var response = cut.handleValidationException(methodArgumentNotValidExceptionMock);

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE);
		assertThat(apiError.getValidationMessages())
				.hasSize(1)
				.first()
				.isEqualTo(METHOD_ARGUMENT_NOT_VALID_VALIDATION_MESSAGE);
	}

	@Test
	@DisplayName("When a ConstraintViolationException occurs, I expect a Bad Request response with an ApiError and "
			+ "an error messagen and 1 validation message")
	void handleValidationExceptionConstraintViolationExceptionExpecttResponse400AndApiErrorWithErrorMessageAndValidationMessage() {
		// GIVEN
		final var violations = validator.validate(getInvalidContact());
		final var constraintViolationException = new ConstraintViolationException(violations);
		assertThat(violations).hasSize(1);

		// WHEN
		final var response = cut.handleValidationException(constraintViolationException);

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(CONSTRAINT_VIOLATION_ERROR_MESSAGE);
		assertThat(Objects.requireNonNull(apiError).getValidationMessages())
				.hasSize(1)
				.first()
				.isEqualTo(CONSTRAINT_VIOLATION_VALIDATION_MESSAGE);
	}

	@Test
	@DisplayName("When a MethodArgumentTypeMismatchException occurs, I expect a Bad Request response with an "
			+ "ApiError and error message, but no validation messages")
	void handleExceptionMethodArgumentTypeMismatchExceptionExpectResponse400AndApiErrorWithErrorMessageButNoValidationMessages() {
		// GIVEN
		final var methodArgumentTypeMismatchException = createArgumentTypeMismatchExceptionMock();

		// WHEN
		final var response = cut.handelException(methodArgumentTypeMismatchException);

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE);
		assertThat(apiError.getValidationMessages()).isEmpty();
	}


	@Test
	@DisplayName("When a WebExchangeBindException occurs, I expect a Bad Request response with an ApiError and "
			+ "error message, but no validation messages")
	void handleExceptionMissingServletRequestParameterExceptionExpectResponse400AndApiErrorWithErrorMessageButNoValidationMessages() {
		// GIVEN
		final var exceptionMessage = "Error!";
		given(webExchangeBindExceptionMock.getMessage()).willReturn(exceptionMessage);

		// WHEN
		final var response = cut.handelException(webExchangeBindExceptionMock);

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(exceptionMessage);
		assertThat(apiError.getValidationMessages()).isEmpty();
	}
	@Test
	@DisplayName("When an unexpected exception occurs, I expect an Internal Server Error response with an ApiError "
			+ "and error message, but no validation messages")
	void handleExceptionExpectResponse400AndApiErrorWithErrorMessageAndNoValidationMessages() {
		// GIVEN / WHEN
		final var response = cut.handelException(new UnexpectedTestException());

		// THEN
		final var apiError = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(Objects.requireNonNull(apiError).getErrorMessage()).isEqualTo(INTERNAL_SERVER_ERROR_MESSAGE);
		assertThat(apiError.getValidationMessages()).isEmpty();
	}

	private Contact getInvalidContact() {
		return createContact().toBuilder().firstName(null).build();
	}

	private MethodArgumentTypeMismatchException createArgumentTypeMismatchExceptionMock() {
		when(methodParameter.getParameterName()).thenReturn(PARAMETER_NAME);
		Mockito.doReturn(String.class).when(methodParameter).getParameterType();
		when(methodArgumentTypeMismatchException.getParameter()).thenReturn(methodParameter);
		return methodArgumentTypeMismatchException;
	}

	private MethodArgumentNotValidException createMethodArgumentNotValidExceptionMock() {
		when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors()).thenReturn(
				List.of(new FieldError("contact", "firstName", "cannot be null")));
		return methodArgumentNotValidException;
	}
}