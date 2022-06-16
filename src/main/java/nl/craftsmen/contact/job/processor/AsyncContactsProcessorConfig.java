package nl.craftsmen.contact.job.processor;

import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import nl.craftsmen.contact.model.ContactWrapper;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AsyncContactsProcessorConfig {

	private final ContactsProcessor contactsProcessor;
	private final ContactsProcessorTaskExecutorConfig taskExecutor;

	@Bean
	public ItemProcessor<ContactWrapper, Future<ContactWrapper>> getAsyncProcessor() {
		AsyncItemProcessor<ContactWrapper, ContactWrapper> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(contactsProcessor);
		asyncItemProcessor.setTaskExecutor(taskExecutor.getTaskExecutor());
		return asyncItemProcessor;
	}
}
