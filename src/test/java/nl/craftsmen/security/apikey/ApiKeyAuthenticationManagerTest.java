package nl.craftsmen.security.apikey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthenticationManagerTest {

	private static final String APIKEY = "dummyApiKey123";
	private static final String SYSTEM_TO_SYSTEM_USER = "s2s";

	@Mock
	private ApiKeyValidator apiKeyValidator;

	@InjectMocks
	private ApiKeyAuthenticationManager cut;

	@Test
	@DisplayName("Given a null value for authentication, I expect that no authentication is being returned when the "
			+ "authenticate method is being called")
	void authenticateExpectNoAuthenticationBecauseAuthenticationIsNull() {
		// GIVEN / WHEN
		final var result = cut.authenticate(null);

		// THEN
		StepVerifier.create(result).verifyComplete();
	}

	@Test
	@DisplayName("Given an authentication object, when authenticate is being called, I expect authentication to be "
			+ "false because the apikey is invalid")
	void authenticateExpectIsAuthenticatedFalseBecauseApiKeyIsNotValid() {
		// GIVEN
		final var authentication = new ApiKeyAuthenticationToken(APIKEY, SYSTEM_TO_SYSTEM_USER);
		given(apiKeyValidator.isApiKeyValid(authentication.getCredentials())).willReturn(false);

		// WHEN
		final var result = cut.authenticate(authentication);

		// THEN
		StepVerifier.create(result)
				.assertNext(r -> assertThat(r.isAuthenticated()).isFalse())
				.verifyComplete();
	}

	@Test
	@DisplayName("Given an authentication object, when authenticate is being called, I expect that authentication is "
			+ "true is because the apikey is valid")
	void authenticateExpectIsAuthenticatedTrueBecauseApiKeyIsValid() {
		// GIVEN
		final var authentication = new ApiKeyAuthenticationToken(APIKEY, SYSTEM_TO_SYSTEM_USER);
		given(apiKeyValidator.isApiKeyValid(authentication.getCredentials())).willReturn(true);

		// WHEN
		final var result = cut.authenticate(authentication);

		// THEN
		StepVerifier.create(result)
				.assertNext(r -> assertThat(r.isAuthenticated()).isTrue())
				.verifyComplete();
	}
}