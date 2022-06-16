package nl.craftsmen.contact.job;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.craftsmen.contact.job.config.ContactsJobParametersConfig;
import nl.craftsmen.exceptionhandling.BatchjobException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ContactsJobRunner {

	private static final String FILE_NAME_CONTEXT_KEY = "filename";

	private final JobLauncher simpleJobLauncher;
	private final ContactsJobParametersConfig contactsJobParametersConfig;
	private final Job processingContactsJob;

	@Async
	public CompletableFuture<Long> runProcessingContactsBatchJob() {
		return CompletableFuture.completedFuture(
				Objects.requireNonNull(runJob(processingContactsJob, contactsJobParametersConfig.getJobParameters()))
						.getJobId());
	}

	private JobExecution runJob(Job job, JobParameters parameters) {
		try {
			return simpleJobLauncher.run(job, parameters);
		} catch (JobExecutionAlreadyRunningException e) {
			throw new BatchjobException(String.format("Job with filename=%s already running.",
					parameters.getParameters().get(FILE_NAME_CONTEXT_KEY)), e);
		} catch (JobRestartException e) {
			throw new BatchjobException(String.format("Job with filename=%s was not started.",
					parameters.getParameters().get(FILE_NAME_CONTEXT_KEY)), e);
		} catch (JobInstanceAlreadyCompleteException e) {
			throw new BatchjobException(String.format("Job with filename=%s was already completed.",
					parameters.getParameters().get(FILE_NAME_CONTEXT_KEY)), e);
		} catch (JobParametersInvalidException e) {
			throw new BatchjobException("Invalid job parameters.", e);
		}
	}
}
