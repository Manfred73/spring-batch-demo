package nl.craftsmen.contact.job.faulthandling;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.util.ConditionalLogger;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class StepSkipListener {

	private final ConditionalLogger logger;

	@OnSkipInRead
	public void onSkipInRead(@NonNull Throwable throwable) {
		logReadError(throwable);
	}

	@OnSkipInProcess
	public void onSkipInProcess(@NonNull ContactWrapper contactWrapper, @NonNull Throwable throwable) {
		logProcessError(contactWrapper.getContactRecord(), throwable);
	}

	@OnSkipInWrite
	public void onSkipInWrite(@NonNull ContactWrapper contactWrapper, @NonNull Throwable throwable) {
		logWriteError(contactWrapper.getContactRecord(), throwable);
	}

	private void logReadError(Throwable throwable) {
		if (throwable instanceof FlatFileParseException) {
			final var exception = (FlatFileParseException) throwable;
			logger.error(log, "Error occurred while reading an item: {}", exception.getInput(), throwable);
		} else {
			logger.error(log, "Error occurred while reading an item!", throwable);
		}
	}

	private void logProcessError(String contact, Throwable throwable) {
		logger.error(log, "Error occurred while processing item {}", contact, throwable);
	}

	private void logWriteError(String contact, Throwable throwable) {
		logger.error(log, "Error occurred while writing an item: {}", contact, throwable);
	}
}