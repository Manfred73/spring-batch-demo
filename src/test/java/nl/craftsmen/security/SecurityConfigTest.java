package nl.craftsmen.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import nl.craftsmen.security.apikey.ApiKeyAuthenticationConverter;
import nl.craftsmen.security.apikey.ApiKeyAuthenticationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

	private static final String[] FILTER_IGNORE = { "/v3/api-docs/**", "/swagger-ui/**", "/actuator/**" };

	@Mock
	private ServerHttpSecurity serverHttpSecurity;

	@Mock
	private ApiKeyAuthenticationManager apiKeyAuthenticationManager;

	@Mock
	private ApiKeyAuthenticationConverter apiKeyAuthenticationConverter;

	@Mock
	private ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec;

	@Mock
	private ServerHttpSecurity.AuthorizeExchangeSpec.Access access;

	@Captor
	private ArgumentCaptor<AuthenticationWebFilter> authenticationWebFilterArgumentCaptor;

	@Mock
	private ServerHttpSecurity.HttpBasicSpec httpBasicSpec;

	@Mock
	private ServerHttpSecurity.CsrfSpec csrfSpec;

	@Mock
	private ServerHttpSecurity.FormLoginSpec formLoginSpec;

	@Mock
	private ServerHttpSecurity.LogoutSpec logoutSpec;

	@Mock
	private SecurityWebFilterChain securityWebFilterChain;

	private SecurityConfig cut = new SecurityConfig();

	@Test
	@DisplayName("Given a ServerHttpSecurity, ApiKeyAuthenticationManager and ApiKeyAuthenticationConverter bean, "
			+ "I expect a SecurityWebFilterChain bean being created with an authenticationWebFilter when "
			+ "springSecurityFilterChain is being called")
	void springSecurityFilterChainExpectASecurityWebFilterChain() {
		// GIVEN
		mockServerHttpSecurity();

		// WHEN
		final var result = cut.springSecurityFilterChain(serverHttpSecurity, apiKeyAuthenticationManager,
				apiKeyAuthenticationConverter);

		// THEN
		assertThat(result).isEqualTo(securityWebFilterChain);
		verify(serverHttpSecurity).addFilterAt(authenticationWebFilterArgumentCaptor.capture(),
				eq(SecurityWebFiltersOrder.AUTHENTICATION));
		final var authenticationWebFilter = authenticationWebFilterArgumentCaptor.getValue();
		final var authenticationConverter = field("authenticationConverter").ofType(ServerAuthenticationConverter.class)
				.in(authenticationWebFilter).get();
		assertThat(authenticationWebFilter).isNotNull();
		assertThat(authenticationConverter).isEqualTo(apiKeyAuthenticationConverter);
	}

	private void mockServerHttpSecurity() {
		given(serverHttpSecurity.authorizeExchange()).willReturn(authorizeExchangeSpec);
		given(authorizeExchangeSpec.pathMatchers(FILTER_IGNORE)).willReturn(access);
		given(access.permitAll()).willReturn(authorizeExchangeSpec);
		given(authorizeExchangeSpec.anyExchange()).willReturn(access);
		given(access.authenticated()).willReturn(authorizeExchangeSpec);
		given(authorizeExchangeSpec.and()).willReturn(serverHttpSecurity);
		given(serverHttpSecurity.addFilterAt(any(), eq(SecurityWebFiltersOrder.AUTHENTICATION))).willReturn(
				serverHttpSecurity);
		given(serverHttpSecurity.httpBasic()).willReturn(httpBasicSpec);
		given(httpBasicSpec.disable()).willReturn(serverHttpSecurity);
		given(serverHttpSecurity.csrf()).willReturn(csrfSpec);
		given(csrfSpec.disable()).willReturn(serverHttpSecurity);
		given(serverHttpSecurity.formLogin()).willReturn(formLoginSpec);
		given(formLoginSpec.disable()).willReturn(serverHttpSecurity);
		given(serverHttpSecurity.logout()).willReturn(logoutSpec);
		given(logoutSpec.disable()).willReturn(serverHttpSecurity);
		given(serverHttpSecurity.build()).willReturn(securityWebFilterChain);
	}
}