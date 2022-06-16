package nl.craftsmen.security.apikey;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApiKeyValidator {

	private final ApiKeyConfig apiKeyConfig;

	public boolean isApiKeyValid(@Nullable Object apikey) {
		if (apikey == null || StringUtils.EMPTY.equals(apikey)) {
			return false;
		}
		return apikey.equals(getValidApiKeyValue());
	}

	private String getValidApiKeyValue() {
		return Optional.ofNullable(apiKeyConfig.getApikey())
				.filter(key -> !StringUtils.EMPTY.equals(key))
				.orElseThrow(() -> new IllegalStateException("No apikey defined"));
	}
}
