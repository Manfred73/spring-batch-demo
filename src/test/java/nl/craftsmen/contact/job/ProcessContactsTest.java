package nl.craftsmen.contact.job;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.util.concurrent.Future;
import nl.craftsmen.contact.job.faulthandling.ItemSkipPolicy;
import nl.craftsmen.contact.job.faulthandling.StepSkipListener;
import nl.craftsmen.contact.job.processor.AsyncContactsProcessorConfig;
import nl.craftsmen.contact.job.writer.AsyncContactsWriterConfig;
import nl.craftsmen.contact.model.ContactWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.step.builder.FaultTolerantStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;

@ExtendWith(MockitoExtension.class)
class ProcessContactsTest {

	@Mock
	private JobBuilderFactory jobBuilderFactory;

	@Mock
	private StepBuilderFactory stepBuilderFactory;

	@Mock
	private ItemStreamReader<ContactWrapper> contactReader;

	private final int chunkSize = 1;

	@Mock
	private ItemSkipPolicy itemSkipPolicy;

	@Mock
	private AsyncContactsProcessorConfig asyncContactsProcessorConfig;

	@Mock
	private ItemProcessor<ContactWrapper, Future<ContactWrapper>> contactItemProcessor;

	@Mock
	private AsyncContactsWriterConfig asyncContactsWriterConfig;

	@Mock
	private ItemWriter<Future<ContactWrapper>> contactItemWriter;

	@Mock
	private StepSkipListener stepSkipListener;

	@Mock
	private JobBuilder jobBuilder;

	@Mock
	private StepBuilder stepBuilder;

	@Mock
	@SuppressWarnings("rawtypes")
	private SimpleStepBuilder simpleStepBuilder;

	@Mock
	private FaultTolerantStepBuilder<ContactWrapper, Future<ContactWrapper>> faultTolerantStepBuilder;

	@Mock
	private TaskletStep taskletStep;

	@Mock
	private SimpleJobBuilder simpleJobBuilder;

	@Mock
	private Job job;

	@Test
	@DisplayName("When processingContactsJob has been called, I expect a Job to be returned")
	void processingContactsJobExpectAJobToBeReturned() {
		// GIVEN
		mockConfigurationSetup();
		final var cut = new ProcessContacts(jobBuilderFactory, stepBuilderFactory,
				contactReader, chunkSize, itemSkipPolicy, asyncContactsProcessorConfig,
				asyncContactsWriterConfig,
				stepSkipListener);

		// WHEN
		final var result = cut.processingContactsJob();

		// THEN
		assertThat(result).isEqualTo(job);
		verifyNoMoreInteractions(jobBuilderFactory, jobBuilder, stepBuilder, simpleStepBuilder,
                asyncContactsProcessorConfig, contactItemProcessor, asyncContactsWriterConfig,
				contactItemWriter, faultTolerantStepBuilder, itemSkipPolicy, stepSkipListener, taskletStep,
				simpleJobBuilder, job);
	}

	@SuppressWarnings("unchecked")
	private void mockConfigurationSetup() {
		given(jobBuilderFactory.get("processingContacts")).willReturn(jobBuilder);
		given(stepBuilderFactory.get("readAndProcessContacts")).willReturn(stepBuilder);
		given(stepBuilder.chunk(chunkSize)).willReturn(simpleStepBuilder);
		given(simpleStepBuilder.reader(contactReader)).willReturn(simpleStepBuilder);
		given(asyncContactsProcessorConfig.getAsyncProcessor()).willReturn(contactItemProcessor);
		given(simpleStepBuilder.processor(contactItemProcessor)).willReturn(simpleStepBuilder);
		given(asyncContactsWriterConfig.getAsyncItemWriter()).willReturn(contactItemWriter);
		given(simpleStepBuilder.writer(contactItemWriter)).willReturn(simpleStepBuilder);
		given(simpleStepBuilder.faultTolerant()).willReturn(faultTolerantStepBuilder);
		given(faultTolerantStepBuilder.skipPolicy(itemSkipPolicy)).willReturn(faultTolerantStepBuilder);
		given(faultTolerantStepBuilder.skip(Exception.class)).willReturn(faultTolerantStepBuilder);
		given(faultTolerantStepBuilder.listener(stepSkipListener)).willReturn(simpleStepBuilder);
		given(simpleStepBuilder.build()).willReturn(taskletStep);
		given(jobBuilder.start(taskletStep)).willReturn(simpleJobBuilder);
		given(simpleJobBuilder.build()).willReturn(job);
	}
}