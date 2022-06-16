package nl.craftsmen.contact.job.writer;

import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import nl.craftsmen.contact.model.ContactWrapper;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AsyncContactsWriterConfig {

	private final NoOpItemWriter noOpItemWriter;

	@Bean
	public ItemWriter<Future<ContactWrapper>> getAsyncItemWriter() {
		AsyncItemWriter<ContactWrapper> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(noOpItemWriter);
		return asyncItemWriter;
	}
}
