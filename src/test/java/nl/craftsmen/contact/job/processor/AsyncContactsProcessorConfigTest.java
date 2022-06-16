package nl.craftsmen.contact.job.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.task.TaskExecutor;

@ExtendWith(MockitoExtension.class)
class AsyncContactsProcessorConfigTest {

	@Mock
	private ContactsProcessor contactsProcessor;

	@Mock
	private ContactsProcessorTaskExecutorConfig contactsProcessorTaskExecutorConfig;

	@Mock
	private TaskExecutor taskExecutor;

	@Test
	@DisplayName("When calling getAsyncProcessor, I expect an ItemProcessor to be returned")
	void getAsyncProcessorExpectAnItemProcessorToBeReturned() {
		// GIVEN
		final var asyncContactProcessor = new AsyncContactsProcessorConfig(contactsProcessor,
				contactsProcessorTaskExecutorConfig);
		given(contactsProcessorTaskExecutorConfig.getTaskExecutor()).willReturn(taskExecutor);

		// WHEN
		final var result = asyncContactProcessor.getAsyncProcessor();

		// THEN
		final var delegate = field("delegate").ofType(ItemProcessor.class).in(result).get();
		final var taskExecutorInAsyncProcessor = field("taskExecutor").ofType(TaskExecutor.class).in(result).get();
		assertThat(result).isInstanceOf(AsyncItemProcessor.class);
		assertThat(delegate).isEqualTo(contactsProcessor);
		assertThat(taskExecutorInAsyncProcessor).isEqualTo(taskExecutor);
	}
}