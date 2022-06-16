package nl.craftsmen.integrationtests;

import static nl.craftsmen.ContactsTestdataSupplier.getUriForRunningBatchjob;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.sql.SQLException;
import nl.craftsmen.contact.ContactsBatchJobController;
import nl.craftsmen.contact.ContactsJobRepositoryCleanService;
import nl.craftsmen.contact.job.ContactsJobRunner;
import nl.craftsmen.exceptionhandling.ApiError;
import nl.craftsmen.exceptionhandling.UnexpectedTestException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

/**
 * In this test class we only test the exception (InternalServerError) situations. All other cases are tested in
 * ContactsBatchJobControllerTest using mocks and in the ContactsSpringBatchIntegrationTest.
 *
 * We disable security, since we don't want to add complexity regarding security in the controller test.
 * Security is tested in the integration test.
 *
 * For more information on WebFluxTest, see https://jstobigdata.com/spring/getting-started-with-spring-webflux
 */
@WebFluxTest(controllers = { ContactsBatchJobController.class }, excludeAutoConfiguration = {
		ReactiveSecurityAutoConfiguration.class })
class InternalExceptionHandlingIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ContactsJobRunner contactsJobRunner;

	@MockBean
	private ContactsJobRepositoryCleanService contactsJobRepositoryCleanService;

	@Test
	@DisplayName("When an unexpected error occurs when starting a batchjob, I expect a response 500 and an ApiError "
			+ "with an error message and without validation messages")
	void findByIdExpectInternalServerErrorAndApiErrorResponse() throws SQLException {
		// GIVEN
		doThrow(new UnexpectedTestException()).when(contactsJobRunner).runProcessingContactsBatchJob();

		// WHEN / THEN
		final var result = webTestClient
				.get()
				.uri(getUriForRunningBatchjob())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is5xxServerError()
				.returnResult(ApiError.class);
		verwachtEenApiError(result);
		verify(contactsJobRepositoryCleanService, never()).removeJobExecutions();
	}

	private void verwachtEenApiError(FluxExchangeResult<ApiError> result) {
		StepVerifier.create(result.getResponseBody())
				.expectNextMatches(a -> StringUtils.isNotEmpty(a.getErrorMessage()))
				.verifyComplete();
	}
}
