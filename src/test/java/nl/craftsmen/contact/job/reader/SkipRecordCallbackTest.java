package nl.craftsmen.contact.job.reader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import nl.craftsmen.util.ConditionalLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class SkipRecordCallbackTest {

	private static final Logger LOG = LoggerFactory.getLogger(SkipRecordCallback.class);

	@Mock
	private ConditionalLogger logger;

	@InjectMocks
	private SkipRecordCallback cut;

	@Test
	@DisplayName("Gegeven een string, als de handleLine methode aangeroepen wordt, verwacht ik dat deze string gelogd wordt")
	void handleLine() {
		// GIVEN
		final var line = "Dit is een regel";

		// WHEN
		cut.handleLine(line);

		// THEN
		verify(logger).info(LOG, "##### Skipping first header record #####: Dit is een regel");
	}

	@Test
	@DisplayName("Als handleLine aangeroepen wordt zonder stromg, verwacht ik een NullPointerException")
	void handleLineZonderStringVerwachtNullPointerException() {
		// GIVEN / WHEN
		final var thrown = catchThrowable(() -> cut.handleLine(null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("line is marked non-null but is null");
		verify(logger, never()).info(LOG, "##### Eerste header record overgeslagen #####: Dit is een regel");
	}
}