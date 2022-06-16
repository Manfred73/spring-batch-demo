package nl.craftsmen.security.apikey;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Checks the incoming {@link Authentication} and verifies that the request should be allowed.
 */
@Component
@AllArgsConstructor
public class ApiKeyAuthenticationManager implements ReactiveAuthenticationManager {

	private final ApiKeyValidator apiKeyValidator;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		return Mono.fromSupplier(() -> doAuthenticate(authentication));
	}

	private Authentication doAuthenticate(Authentication authentication) {
		if (authentication != null && apiKeyValidator.isApiKeyValid(authentication.getCredentials())) {
			authentication.setAuthenticated(true);
		}
		return authentication;
	}
}
