package nl.craftsmen.contact.job.reader;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.craftsmen.util.ConditionalLogger;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SkipRecordCallback implements LineCallbackHandler {

	private final ConditionalLogger logger;

	@Override
	public void handleLine(@NonNull String line) {
		logger.info(log, "##### Skipping first header record #####: " + line);
	}
}
