package nl.craftsmen.contact.job;

import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import nl.craftsmen.contact.job.faulthandling.ItemSkipPolicy;
import nl.craftsmen.contact.job.faulthandling.StepSkipListener;
import nl.craftsmen.contact.job.processor.AsyncContactsProcessorConfig;
import nl.craftsmen.contact.job.writer.AsyncContactsWriterConfig;
import nl.craftsmen.contact.model.ContactWrapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Some thoughts about using AsyncItemProcessor and AsyncItemWriter in Spring batch:
 *
 * - https://stackoverflow.com/questions/62075902/why-is-exception-in-spring-batch-asycitemprocessor-caught-by-skiplisteners-onsk
 * - https://quabr.com/62075902/why-is-exception-in-spring-batch-asycitemprocessor-caught-by-skiplisteners-onsk
 * - https://blog.codecentric.de/en/2014/03/skipping-asynchronous-batch-processing/
 * </pre>
 */
@Configuration
@AllArgsConstructor
public class ProcessContacts {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final ItemStreamReader<ContactWrapper> contactsReader;
	private final int chunkSize;
	private final ItemSkipPolicy itemSkipPolicy;
	private final AsyncContactsProcessorConfig asyncContactsProcessorConfig;
	private final AsyncContactsWriterConfig asyncContactsWriterConfig;
	private final StepSkipListener stepSkipListener;

	@Qualifier(value = "processContacts")
	@Bean
	public Job processingContactsJob() {
		return jobBuilderFactory.get("processingContacts")
				.start(readAndProcessContacts())
				.build();
	}

	private Step readAndProcessContacts() {
		return stepBuilderFactory.get("readAndProcessContacts")
				.<ContactWrapper, Future<ContactWrapper>>chunk(chunkSize)
				.reader(contactsReader)
				.processor(asyncContactsProcessorConfig.getAsyncProcessor())
				.writer(asyncContactsWriterConfig.getAsyncItemWriter())
				.faultTolerant()
				.skipPolicy(itemSkipPolicy)    // unlimited skip
				.skip(Exception.class)         // ignore all Exceptions, so that next record will be processed
				.listener(stepSkipListener)
				.build();
	}
}
