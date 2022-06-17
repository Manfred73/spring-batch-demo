package nl.craftsmen.security;

import nl.craftsmen.security.apikey.ApiKeyAuthenticationConverter;
import nl.craftsmen.security.apikey.ApiKeyAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * Inspired by https://github.com/gregwhitaker/springboot-webflux-apikey-example/tree/master/src/main/java/example/security.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private static final String[] FILTER_IGNORE = {
			// -- Swagger UI v3 (OpenAPI)
			"/v3/api-docs/**",
			"/swagger-ui/**",
			// Actuator public endpoints
			"/actuator/**"
	};

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
        ApiKeyAuthenticationManager apiKeyAuthenticationManager, ApiKeyAuthenticationConverter apiKeyAuthenticationConverter) {

		final var authenticationWebFilter = new AuthenticationWebFilter(apiKeyAuthenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(apiKeyAuthenticationConverter);

		return http.authorizeExchange()
				.pathMatchers(FILTER_IGNORE)
				.permitAll()
				.anyExchange()
				.authenticated()
				.and()
				.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.httpBasic()
				.disable()
				.csrf()
				.disable()
				.formLogin()
				.disable()
				.logout()
				.disable()
				.build();
	}
}
