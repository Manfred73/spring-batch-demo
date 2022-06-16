package nl.craftsmen.contact.job.faulthandling;

import lombok.NonNull;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Component;

@Component
public class ItemSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(final @NonNull Throwable throwable, final int skipCount) throws SkipLimitExceededException {
		return true;
	}
}
