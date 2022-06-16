package nl.craftsmen.security.apikey;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents an authentication token within the application.
 */
public class ApiKeyAuthenticationToken implements Authentication {

	private static final long serialVersionUID = 3451535026012161519L;

	private final String apiKey;
	private final String principal;
	private boolean authenticated = false;

	public ApiKeyAuthenticationToken(final String apiKey, final String principal) {
		this.apiKey = apiKey;
		this.principal = principal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return this.apiKey;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

	@Override
	public String getName() {
		return this.principal;
	}
}
