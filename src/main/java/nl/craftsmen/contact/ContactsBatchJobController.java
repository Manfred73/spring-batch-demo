package nl.craftsmen.contact;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import nl.craftsmen.contact.job.ContactsJobRunner;
import nl.craftsmen.contact.model.Batchjob;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ContactsBatchJobController.RESOURCE)
@AllArgsConstructor
@Slf4j
public class ContactsBatchJobController {

	static final String RESOURCE = "/batchjob";

	private final ContactsJobRunner contactsJobRunner;
	private final ContactsJobRepositoryCleanService contactsJobRepositoryCleanService;

	@GetMapping("/run")
	public Batchjob run() throws ExecutionException, InterruptedException, JobInstanceAlreadyCompleteException,
			JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		log.info(">>>>> Resource /batchjob/run called");
		final var batchjobId = contactsJobRunner.runProcessingContactsBatchJob().get();
		return Batchjob.builder().batchjobId(batchjobId).build();
	}

	@GetMapping("/removejobexecutions")
	public String removeJobExecutions() throws SQLException {
		log.info(">>>>> Resource /batchjob/removejobexecutions called");
		contactsJobRepositoryCleanService.removeJobExecutions();
		return "OK";
	}
}
