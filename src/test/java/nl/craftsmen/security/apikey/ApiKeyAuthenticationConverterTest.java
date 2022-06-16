package nl.craftsmen.security.apikey;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthenticationConverterTest {

	private static final String APIKEY = "dummyApiKey123";
	private static final String API_KEY_HEADER_NAME = "apikey";
	private static final String SYSTEM_TO_SYSTEM_USER = "s2s";

	@Mock
	private ServerWebExchange serverWebExchange;

	@Mock
	private ServerHttpRequest serverHttpRequest;

	@Mock
	private HttpHeaders httpHeaders;

	private final ApiKeyAuthenticationConverter converter = new ApiKeyAuthenticationConverter();

	@Test
	@DisplayName("Given a ServerWebExchange containing an apikey in the header, I expect an Authentication object to "
			+ "terug be returned containing the credentials (apikey) and a principal")
	void convertExpectAnAuthenticationObjectWithCredentialsandPrincipal() {
		// GIVEN
		given(serverWebExchange.getRequest()).willReturn(serverHttpRequest);
		given(serverHttpRequest.getHeaders()).willReturn(httpHeaders);
		given(httpHeaders.get(API_KEY_HEADER_NAME)).willReturn(List.of(APIKEY));

		// WHEN
		final var result = converter.convert(serverWebExchange);

		// THEN
		StepVerifier.create(result)
				.assertNext(this::assertResult)
				.verifyComplete();
	}

	@Test
	@DisplayName("Given a ServerWebExchange without apikey in the header, I expect no Authentication object to be "
			+ "returned containing the credentials (apikey) and a principal")
	void convertExpectNoAuthenticationObjectWithCredentialsAndPrincipal() {
		// GIVEN
		given(serverWebExchange.getRequest()).willReturn(serverHttpRequest);
		given(serverHttpRequest.getHeaders()).willReturn(httpHeaders);
		given(httpHeaders.get(API_KEY_HEADER_NAME)).willReturn(emptyList());

		// WHEN
		final var result = converter.convert(serverWebExchange);

		// THEN
		StepVerifier.create(result).verifyComplete();
	}

	private void assertResult(Authentication authentication) {
		final var credentials = (String) authentication.getCredentials();
		assertThat(credentials).isEqualTo(APIKEY);
		assertThat(authentication.getPrincipal()).isEqualTo(SYSTEM_TO_SYSTEM_USER);
	}
}