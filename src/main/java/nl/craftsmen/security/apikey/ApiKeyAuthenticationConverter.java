package nl.craftsmen.security.apikey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Converter that gets the API key from the incoming headers and converts it to an {@link Authentication} that can be checked by the
 * {@link ApiKeyAuthenticationManager}.
 */
@Component
@Slf4j
public class ApiKeyAuthenticationConverter implements ServerAuthenticationConverter {
	private static final String API_KEY_HEADER_NAME = "apikey";
	private static final String SYSTEM_TO_SYSTEM_USER = "s2s";

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange)
				.flatMap(
						serverWebExchange -> Mono.justOrEmpty(serverWebExchange.getRequest().getHeaders().get(API_KEY_HEADER_NAME)))
				.filter(headerValues -> !headerValues.isEmpty())
				.flatMap(headerValues -> lookup(headerValues.get(0)));
	}

	/**
	 * When using user based api keys, the principal could be looked up from a database to check if the correct apikey for a user
	 * was provided. In this case we only have an apikey for system to system usage, therefore we set the principal to an s2s user.
	 */
	private Mono<ApiKeyAuthenticationToken> lookup(final String apiKey) {
		return Mono.just(new ApiKeyAuthenticationToken(apiKey, SYSTEM_TO_SYSTEM_USER));
	}
}
