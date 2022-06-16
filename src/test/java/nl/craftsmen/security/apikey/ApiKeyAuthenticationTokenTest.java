package nl.craftsmen.security.apikey;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiKeyAuthenticationTokenTest {

	private static final String APIKEY = "dummyApiKey123";
	private static final String SYSTEM_TO_SYSTEM_USER = "s2s";

	@Test
	@DisplayName("Given an apikey and a principal, I expect an ApiKeyAuthenticationToken to be returned when calling "
			+ "the constructor with these arguments, that credentials and principal are set, that name is equal to "
			+ "principal, that authorities are empty, that details is null and that authenticated is false")
	void constructApiKeyAuthenticationToken() {
		// GIVEN / WHEN
		final var authentication = new ApiKeyAuthenticationToken(APIKEY, SYSTEM_TO_SYSTEM_USER);

		// THEN
		assertThat(authentication)
				.extracting(
						ApiKeyAuthenticationToken::getCredentials,
						ApiKeyAuthenticationToken::getPrincipal,
						ApiKeyAuthenticationToken::getName,
						ApiKeyAuthenticationToken::getAuthorities,
						ApiKeyAuthenticationToken::getDetails,
						ApiKeyAuthenticationToken::isAuthenticated)
				.containsExactly(
						APIKEY,
						SYSTEM_TO_SYSTEM_USER,
						SYSTEM_TO_SYSTEM_USER,
						Collections.emptyList(),
						null,
						false
				);
	}

	@Test
	@DisplayName("Given ApiKeyAuthenticationToken, when authenticated is set to true, I expect it to be true")
	void setAuthenticated() {
		// GIVEN
		final var authentication = new ApiKeyAuthenticationToken(APIKEY, SYSTEM_TO_SYSTEM_USER);

		// THEN
		assertThat(authentication.isAuthenticated()).isFalse();

		// WHEN
		authentication.setAuthenticated(true);

		// THEN
		assertThat(authentication.isAuthenticated()).isTrue();
	}
}