package nl.craftsmen.contact.job.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class ContactsProcessorTaskExecutorConfigTest {

	@Test
	@DisplayName("Given several properties, I expect a ThreadPoolTaskExecutor to be returned which contain these " +
			"properties")
	void getTaskExecutor() {
		// GIVEN
		final var contactProcessorTaskExecutor = new ContactsProcessorTaskExecutorConfig(24, 24, 20);

		// WHEN
		final var taskExecutor = contactProcessorTaskExecutor.getTaskExecutor();

		// THEN
		final var queueCapacity = field("queueCapacity").ofType(int.class).in(taskExecutor).get();
		final var waitForTasksToCompleteOnShutdown =
				field("waitForTasksToCompleteOnShutdown").ofType(boolean.class).in(taskExecutor).get();
		assertThat(taskExecutor)
				.extracting(
						t -> ((ThreadPoolTaskExecutor) t).getCorePoolSize(),
						t -> ((ThreadPoolTaskExecutor) t).getMaxPoolSize(),
						t -> ((ThreadPoolTaskExecutor) t).getThreadNamePrefix()
				)
				.containsExactly(
						24,
						24,
						"MultiThreadedContactProcessorTaskExecutor-");
		assertThat(((ThreadPoolTaskExecutor) taskExecutor).getThreadPoolExecutor().getRejectedExecutionHandler())
				.isInstanceOf(ThreadPoolExecutor.CallerRunsPolicy.class);
		assertThat(queueCapacity).isEqualTo(20);
		assertThat(waitForTasksToCompleteOnShutdown).isTrue();
	}
}