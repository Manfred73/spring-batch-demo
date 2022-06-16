package nl.craftsmen.integrationtests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		// Usually users, passwords and apikeys are read from the environment.
		// For the integrationtest we specify dummy values.
		"security.apikey=dummyContactsProcessorApikey",
		"contactsclient.baseUrl=http://localhost:${wiremock.server.port}/v2/contacts",
		"contactsclient.apikey=dummyContactsApikey"
})
@AutoConfigureWebTestClient(timeout = "36000")
@AutoConfigureWireMock(port = 0)
@Testcontainers
@ActiveProfiles("integrationtest")
abstract class BaseIntegrationTest {

	@Autowired
	protected WebTestClient webTestClient;

	@SuppressWarnings("rawtypes")
	public static PostgreSQLContainer postgreSQL =
			new PostgreSQLContainer("postgres:14.3")
					.withUsername("postgres")
					.withPassword("postgres")
					.withDatabaseName("postgres");

	static {
		postgreSQL.start();
	}

	@DynamicPropertySource
	static void postgresqlProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQL::getUsername);
		registry.add("spring.datasource.password", postgreSQL::getPassword);
	}
}
