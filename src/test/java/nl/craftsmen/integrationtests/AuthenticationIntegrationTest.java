package nl.craftsmen.integrationtests;

import static nl.craftsmen.ContactsTestdataSupplier.RESOURCE_PATH;
import static nl.craftsmen.ContactsTestdataSupplier.getUriForRunningBatchjob;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationIntegrationTest extends BaseIntegrationTest {

	private static final String HEADER_NAME_APIKEY = "apikey";
	private static final String VALID_APIKEY_VALUE = "dummyContactsProcessorApikey";
	private static final String INVALID_APIKEY = "letsTryEvilHackerApiKey";

	@Autowired
	private WebTestClient webTestClient;

	@Test
	@DisplayName("When a get request is done with a valid apikey in the header for starting a batchjob, I expect a "
			+ "response 200 (OK)")
	void expect_200_response_when_apikey_is_valid() {
		webTestClient.get()
				.uri(getUriForRunningBatchjob())
				.header(HEADER_NAME_APIKEY, VALID_APIKEY_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	@DisplayName("When a get request is done with an invalid apikey in the header for starting a batchjob, I expect a "
			+ "response 403 (forbidden)")
	void expect_403_response_when_apikey_is_invalid() {
		webTestClient.get()
				.uri(RESOURCE_PATH)
				.header(HEADER_NAME_APIKEY, INVALID_APIKEY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isForbidden();
	}

	@Test
	@DisplayName("When a get request is done with no apikey in the header for starting a batchjob, I expect a "
			+ "response 401 (unauthorized)")
	void expect_401_response_when_authentication_header_is_empty() {
		webTestClient.get()
				.uri(RESOURCE_PATH)
				.exchange()
				.expectStatus().isUnauthorized();
	}
}
