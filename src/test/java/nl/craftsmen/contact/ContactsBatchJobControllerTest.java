package nl.craftsmen.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import nl.craftsmen.contact.job.ContactsJobRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

@ExtendWith(MockitoExtension.class)
class ContactsBatchJobControllerTest {

    @Mock
    private ContactsJobRunner contactsJobRunner;

    @Mock
    private ContactsJobRepositoryCleanService contactsJobRepositoryCleanService;

    @Mock
    private CompletableFuture<Long> jobExecutionCompletableFuture;

    @InjectMocks
    private ContactsBatchJobController cut;

    @Test
    @DisplayName("When the run method is being called, I expect that the batch job has been started and that a "
            + "CompletableFuture<JobExecution> will be returned")
    void runExpectThatBatchjobHasBeenStarted() throws ExecutionException, InterruptedException,
            JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException,
            JobRestartException {
        // GIVEN
        final var jobId = 101L;
        willReturn(jobExecutionCompletableFuture).given(contactsJobRunner).runProcessingContactsBatchJob();
        given(jobExecutionCompletableFuture.get()).willReturn(jobId);

        // WHEN
        final var result = cut.run();

        // THEN
        assertThat(result.getBatchjobId()).isEqualTo(jobId);
        verify(contactsJobRunner).runProcessingContactsBatchJob();
    }

    @Test
    @DisplayName("When the removeJobExecutions method is being called, I expect that "
            + "contactsJobRepositoryCleanService.removeJobExecutions has been called")
    void removeJobExecutionsExpectThatRemoveJobExecutionsHasBeenCalled() throws SQLException {
        // GIVEN / WHEN
        final var result = cut.removeJobExecutions();

        // THEN
        assertThat(result).isEqualTo("OK");
        verify(contactsJobRepositoryCleanService).removeJobExecutions();
    }
}
