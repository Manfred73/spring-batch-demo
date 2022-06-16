package nl.craftsmen.contact.job;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import nl.craftsmen.contact.job.config.ContactsJobParametersConfig;
import nl.craftsmen.exceptionhandling.BatchjobException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

@ExtendWith(MockitoExtension.class)
class ContactsJobRunnerTest {

	@Mock
	private JobLauncher simpleJobLauncher;

	@Mock
	private ContactsJobParametersConfig contactsJobParametersConfig;

	@Mock
	private JobParameters jobParameters;

	@Mock
	private Map<String, JobParameter> jobParameterMap;

/*
	@Mock
	private JobParameter jobParameter;
*/

	@Mock
	private Job processingContactsJob;

	@Mock
	private JobExecution jobExecution;

	@InjectMocks
	private ContactsJobRunner cut;

	@Test
	@DisplayName("When runProcessingContactsBatchJob has been called, I expect that the batchjob with the correct "
			+ "parameters has been called and that no exception occurred")
	void runProcessingContactsBatchJobExpectBatchjobStartedWithCorrectParametersAndExpectNoException()
			throws ExecutionException, InterruptedException, JobInstanceAlreadyCompleteException,
			JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		// GIVEN
		final var jobId = 109L;
		given(contactsJobParametersConfig.getJobParameters()).willReturn(jobParameters);
		willReturn(jobExecution).given(simpleJobLauncher).run(processingContactsJob, jobParameters);
		given(jobExecution.getJobId()).willReturn(jobId);

		// WHEN
		final var result = cut.runProcessingContactsBatchJob();

		// THEN
		assertThat(result.get()).isEqualTo(jobId);
		verify(simpleJobLauncher).run(processingContactsJob, jobParameters);
		verifyNoMoreInteractions(simpleJobLauncher, contactsJobParametersConfig, processingContactsJob);
	}

	@Test
	@DisplayName("When runProcessingContactsBatchJob has been called and if a JobExecutionAlreadyRunningException, "
					+ "occurred, I expect a BatchjobException")
	void runProcessingContactsBatchJobWhenJobExecutionAlreadyRunningExceptionOccursExpectBatchjobException()
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
			JobRestartException {
		// GIVEN
		final var exception = new JobExecutionAlreadyRunningException("Job already running");
		final var jobParameter = new JobParameter("file.txt");
		given(contactsJobParametersConfig.getJobParameters()).willReturn(jobParameters);
		given(jobParameters.getParameters()).willReturn(jobParameterMap);
		given(jobParameterMap.get("filename")).willReturn(jobParameter);
		given(simpleJobLauncher.run(processingContactsJob, jobParameters)).willThrow(exception);

		// WHEN
		final var thrown = catchThrowable(() -> cut.runProcessingContactsBatchJob());

		// THEN
		assertThat(thrown)
				.isInstanceOf(BatchjobException.class)
				.hasMessage("Job with filename=file.txt already running.")
				.hasCause(exception)
				.hasRootCauseMessage("Job already running");

		// THEN
		verifyNoMoreInteractions(simpleJobLauncher, processingContactsJob);
	}

	@Test
	@DisplayName("When runProcessingContactsBatchJob has been called and if a JobRestartException occurred, I expect " +
			"a BatchjobException")
	void runProcessingContactsBatchJobWhenJobRestartExceptionOccursExpectBatchjobException()
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
			JobRestartException {
		// GIVEN
		final var exception = new JobRestartException("Job was not started");
		final var jobParameter = new JobParameter("file.txt");
		given(contactsJobParametersConfig.getJobParameters()).willReturn(jobParameters);
		given(jobParameters.getParameters()).willReturn(jobParameterMap);
		given(jobParameterMap.get("filename")).willReturn(jobParameter);
		given(simpleJobLauncher.run(processingContactsJob, jobParameters)).willThrow(exception);

		// WHEN
		final var thrown = catchThrowable(() -> cut.runProcessingContactsBatchJob());

		// THEN
		assertThat(thrown)
				.isInstanceOf(BatchjobException.class)
				.hasMessage("Job with filename=file.txt was not started.")
				.hasCause(exception)
				.hasRootCauseMessage("Job was not started");

		// THEN
		verifyNoMoreInteractions(simpleJobLauncher, processingContactsJob);
	}

	@Test
	@DisplayName("When runProcessingContactsBatchJob has been called and if a JobInstanceAlreadyCompleteException, "
			+ "occurred, I expect a BatchjobException")
	void runProcessingContactsBatchJobWhenJobInstanceAlreadyCompleteExceptionOccursExpectBatchjobException()
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
			JobRestartException {
		// GIVEN
		final var exception = new JobInstanceAlreadyCompleteException("Job was already completed");
		final var jobParameter = new JobParameter("file.txt");
		given(contactsJobParametersConfig.getJobParameters()).willReturn(jobParameters);
		given(jobParameters.getParameters()).willReturn(jobParameterMap);
		given(jobParameterMap.get("filename")).willReturn(jobParameter);
		given(simpleJobLauncher.run(processingContactsJob, jobParameters)).willThrow(exception);

		// WHEN
		final var thrown = catchThrowable(() -> cut.runProcessingContactsBatchJob());

		// THEN
		assertThat(thrown)
				.isInstanceOf(BatchjobException.class)
				.hasMessage("Job with filename=file.txt was already completed.")
				.hasCause(exception)
				.hasRootCauseMessage("Job was already completed");

		// THEN
		verifyNoMoreInteractions(simpleJobLauncher, processingContactsJob);
	}

	@Test
	@DisplayName("When runProcessingContactsBatchJob has been called and if a JobParametersInvalidException, occurred "
			+ "I expect a BatchjobException")
	void runProcessingContactsBatchJobWhenJobParametersInvalidExceptionOccursExpectBatchjobException()
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
			JobRestartException {
		// GIVEN
		final var exception = new JobParametersInvalidException("Job is invalid");
		given(contactsJobParametersConfig.getJobParameters()).willReturn(jobParameters);
		given(simpleJobLauncher.run(processingContactsJob, jobParameters)).willThrow(exception);

		// WHEN
		final var thrown = catchThrowable(() -> cut.runProcessingContactsBatchJob());

		// THEN
		assertThat(thrown)
				.isInstanceOf(BatchjobException.class)
				.hasMessage("Invalid job parameters.")
				.hasCause(exception)
				.hasRootCauseMessage("Job is invalid");

		// THEN
		verifyNoMoreInteractions(simpleJobLauncher, processingContactsJob);
	}
}