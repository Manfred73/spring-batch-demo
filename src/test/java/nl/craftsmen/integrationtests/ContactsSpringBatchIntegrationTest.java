package nl.craftsmen.integrationtests;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.Date;

import nl.craftsmen.contact.model.Batchjob;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBatchTest
@ExtendWith(SpringExtension.class)
class ContactsSpringBatchIntegrationTest extends BaseIntegrationTest {

    private static final String RESOURCE_PATH_V2_CONTACTS = "/v2/contacts";
    private static final String RESOURCE_PATH_BATCHJOB = "/batchjob";
    private static final String HEADER_NAME_APIKEY = "apikey";
    private static final String API_KEY_VALUE_CONTACTS_PROCESSOR = "dummyContactsProcessorApikey";
    private static final String API_KEY_VALUE_CONTACTS = "dummyContactsApikey";
    private static final String HEADER_CONTENT_TYPE = "Content-type";
    private static final String DATE_CONTEXT_KEY = "date";
    private static final String FILE_NAME_CONTEXT_KEY = "filename";

    @Autowired
    private WebTestClient webTestClient;

    /**
     * <pre>
     * @SpringBatchTest creates and injects jobLauncherTestUtils and jobRepositoryTestUtils in the test context.
     * IntelliJ IDEA is not able to introspect that hence the error (or warning, depending on the level you've set).
     * See also:
     *   https://stackoverflow.com/questions/67767525/could-not-autowire-no-beans-of-jobrepositorytestutils-type-found-with-spring
     * </pre>
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    void setup() {
        jobRepositoryTestUtils.removeJobExecutions();
        WireMock.reset();
    }

    @AfterEach
    public void teardown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    /**
     * <pre>
     * This integrationtest is triggered by calling the batchjob/run resource and processes the file contacts.txt from
     * src/test/resources (and not the file contacts.txt from src/main/resources).
     * </pre>
     */
    @Test
    @DisplayName("Given an input file with 5 contact records, I expect that this file is processed when the batchjob "
            + "is started and that 5 records have been sent to the contacts service")
    void expectThat5ContactRecordsHaveBeenProcessedAndPostedToTheContactService() {
        // GIVEN
        stubMockForContactsPost();

        // WHEN
        webTestClient
                .get()
                .uri(RESOURCE_PATH_BATCHJOB + "/run")
                .header(HEADER_NAME_APIKEY, API_KEY_VALUE_CONTACTS_PROCESSOR)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Batchjob.class)
                .value(batchjob -> assertThat(batchjob.getBatchjobId()).isNotNull());

        // THEN
        verify(5, postRequestedFor(urlEqualTo(RESOURCE_PATH_V2_CONTACTS)));
    }

    /**
     * <pre>
     * In this integrationtest we want to use another input file with 5 records of which 1 record is faulty.
     * We cannot do this through the batchjob/run resource, because this will use the wrong file (since it is
     * hardcoded for the demo). We can use the jobLauncherTestUtils to overrule the batch job parameters and to trigger
     * the batchjob ourselves (see https://www.baeldung.com/spring-batch-testing-job).
     * </pre>
     */
    @Test
    @DisplayName("Given an input file with 5 contact records of which 1 is faulty, I expect that this file is "
            + "processed when the batchjob is started and that 4 records will be processed successfully and have been "
            + "sent to the contacts service")
    void expectThat5ContactRecordsHaveBeenProcessedOfWhich1WasFaultyAndOfWhich4HaveBeenSentToTheContactService()
            throws Exception {
        // GIVEN
        stubMockForContactsPost();

        // WHEN
        final var jobExecution = jobLauncherTestUtils.launchJob(createJobParametersVoorFileMet1FoutiefRecord());
        final var actualJobInstance = jobExecution.getJobInstance();
        final var actualJobExitStatus = jobExecution.getExitStatus();

        // THEN
        assertThat(actualJobInstance.getJobName()).isEqualTo("processingContacts");
        assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
        verify(4, postRequestedFor(urlEqualTo(RESOURCE_PATH_V2_CONTACTS)));
    }

    private void stubMockForContactsPost() {
        stubFor(
                post(urlPathMatching(RESOURCE_PATH_V2_CONTACTS))
                        .withHeader(HEADER_NAME_APIKEY, matching(API_KEY_VALUE_CONTACTS))
                        .willReturn(
                                aResponse()
                                        .withHeader(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withStatus(HttpStatus.OK.value())));
    }

    private JobParameters createJobParametersVoorFileMet1FoutiefRecord() {
        final var jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(FILE_NAME_CONTEXT_KEY, "contacts_with_1_faulty_record.txt");
        jobParametersBuilder.addDate(DATE_CONTEXT_KEY, new Date(), true);
        return jobParametersBuilder.toJobParameters();
    }
}
