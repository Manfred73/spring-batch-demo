package nl.craftsmen.contact.job.faulthandling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.verify;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.exceptionhandling.UnexpectedTestException;
import nl.craftsmen.util.ConditionalLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileParseException;

@ExtendWith(MockitoExtension.class)
class StepSkipListenerTest {

	private static final Logger LOG = LoggerFactory.getLogger(StepSkipListener.class);

	@Mock
	private ConditionalLogger logger;

	@InjectMocks
	private StepSkipListener cut;

	@Test
	@DisplayName("Given an exception, when onSkipInRead is called, I expect an error has been logged")
	void onSkipInReadExpectAnErrorHasBeenLogged() {
		// GIVEN
		final var exception = new UnexpectedTestException();

		// WHEN
		cut.onSkipInRead(exception);

		// THEN
		verify(logger).error(LOG, "Error occurred while reading an item!", exception);
	}

	@Test
	@DisplayName("When onSkipInRead is called without an exception, I expect a NullPointerException")
	void onSkipInReadWithoutThrowableExpectNullPointerException() {
		// GIVEN / WHEN
		final var thrown = catchThrowable(() -> cut.onSkipInRead(null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("throwable is marked non-null but is null");
	}

	@Test
	@DisplayName("Given a FlatFileParseException, when onSkipInRead is called, I expect an error has been loged")
	void onSkipInReadExpectThatReadErrorHasBeenCalledBecauseOfFlatFileParseExceptionAndErrorHasBeenLogged() {
		// GIVEN
		final var exception = new FlatFileParseException("Oh no, an error!", "Help!");

		// WHEN
		cut.onSkipInRead(exception);

		// THEN
		verify(logger).error(LOG, "Error occurred while reading an item: {}", "Help!", exception);
	}

	@Test
	@DisplayName("Given a contactWrapper and an exception, when onSkipInProcess is called, I expect an error has been " +
			"logged")
	void onSkipInProcessExpectThatProcessErrorHasBeenCalledAndAnErrorHasBeenLogged() {
		// GIVEN
		final var contactRecord = "This is a contact json object";
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contactRecord)
				.build();
		final var exception = new UnexpectedTestException();

		// WHEN
		cut.onSkipInProcess(contactWrapper, exception);

		// THEN
		verify(logger).error(LOG, "Error occurred while processing item {}", contactRecord, exception);
	}

	@Test
	@DisplayName("Given a contactWrapper and no exception, when onSkipInProcess is called, I expect a " +
			"NullPointerException because exception is null")
	void onSkipInProcessWithoutThrowableExpectNullpointerException() {
		// GIVEN
		final var contactRecord = "This is a contact json object";
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contactRecord)
				.build();

		// WHEN
		final var thrown = catchThrowable(() -> cut.onSkipInProcess(contactWrapper, null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("throwable is marked non-null but is null");
	}

	@Test
	@DisplayName("Given an exception and no contactWrapper, when onSkipInProcess is called, I expect a "
			+ "NullPointerException because contactWrapper is null")
	void onSkipInProcessWithoutContactWrapperExpectNullpointerException() {
		// GIVEN
		final var exception = new UnexpectedTestException();

		// WHEN
		final var thrown = catchThrowable(() -> cut.onSkipInProcess(null, exception));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("contactWrapper is marked non-null but is null");
	}

	@Test
	@DisplayName("Given a contactWrapper and an exception, when onSkipInWrite is called, I expect an error has been " +
			"logged")
	void onSkipInWriteExpectThatLogWriteHasBeenCalledAndErrorHasBeenLogged() {
		// GIVEN
		final var contactRecord = "This is a contact json object";
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contactRecord)
				.build();
		final var exception = new UnexpectedTestException();

		// WHEN
		cut.onSkipInWrite(contactWrapper, exception);

		// THEN
		verify(logger).error(LOG, "Error occurred while writing an item: {}", contactRecord, exception);
	}

	@Test
	@DisplayName("Given a contactWrapper and no exception, when onSkipInWrite is called, I expect a "
			+ "NullPointerException because exception is null")
	void onSkipInWriteWithoutContactWrapperNullExpectNullpointerException() {
		// GIVEN
		final var contactRecord = "This is a contact json object";
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contactRecord)
				.build();

		// WHEN
		final var thrown = catchThrowable(() -> cut.onSkipInWrite(contactWrapper, null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("throwable is marked non-null but is null");
	}

	@Test
	@DisplayName("Given an exception and no contactWrapper, when onSkipInWrite is called, I expect a "
			+ "NullPointerException because exception is null")
	void onSkipInWriteWithThrowableNullExpectNullpointerException() {
		// GIVEN
		final var exception = new UnexpectedTestException();

		// WHEN
		final var thrown = catchThrowable(() -> cut.onSkipInWrite(null, exception));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("contactWrapper is marked non-null but is null");
	}
}