package nl.craftsmen.security.apikey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.BDDMockito.given;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.lang.Nullable;

@ExtendWith(MockitoExtension.class)
class ApiKeyValidatorTest {

	private static final String VALID_API_KEY = "validApiKey";
	private static final String INVALID_API_KEY = "invalidApiKey";

	@Mock
	private ApiKeyConfig apiKeyConfig;

	@InjectMocks
	private ApiKeyValidator cut;

	@ParameterizedTest(name = "{index}: isApiKeyValid {0}, want {1}")
	@MethodSource("apiKeyGenerator")
	@DisplayName("Given an apikey, I expect that it is not valid (false) or valid (true)")
	void expect_validateKey_to_return_true_when_valid_api_key_is_supplied(boolean expectedResult,
			@Nullable String suppliedApiKey) {
		// GIVEN
		mockApiKeyConfigBasedOnSuppliedApiKey(suppliedApiKey);

		// WHEN
		final var result = cut.isApiKeyValid(suppliedApiKey);

		// THEN
		assertThat(result).isEqualTo(expectedResult);
	}

	@ParameterizedTest(name = "{index}: IllegalStateException, want {0}")
	@MethodSource("apiKeyGeneratorIllegalStateException")
	@DisplayName("Given an apikey from the config file with a null or empty string value, I expect an "
			+ "IllegalStateException will be thrown")
	void expect_validateKey_to_return_IllegalStateException_when_apikeyFromConfig_is_null_or_empty(String apikeyFromConfig) {
		// GIVEN
		mockApiKeyConfigBasedOnApiKeyFromConfig(apikeyFromConfig);

		// WHEN
		Throwable thrown = catchThrowable(() -> cut.isApiKeyValid(VALID_API_KEY));

		// THEN
		assertThat(thrown)
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("No apikey defined");
	}

	private static Stream<Arguments> apiKeyGeneratorIllegalStateException() {
		return Stream.of(
				Arguments.of(Named.of("apikey is null", null)),
				Arguments.of(Named.of("apikey is an empty String", StringUtils.EMPTY)));
	}


	private static Stream<Arguments> apiKeyGenerator() {
		return Stream.of(
				Arguments.of(false, Named.of("apikey is null", null)),
				Arguments.of(false, Named.of("apikey is an empty String", StringUtils.EMPTY)),
				Arguments.of(false, Named.of("apikey is invalid", INVALID_API_KEY)),
				Arguments.of(true, Named.of("apikey is valid", VALID_API_KEY)));
	}

	private void mockApiKeyConfigBasedOnSuppliedApiKey(String suppliedApiKey) {
		if (StringUtils.isNotEmpty(suppliedApiKey)) {
			given(apiKeyConfig.getApikey()).willReturn(VALID_API_KEY);
		}
	}

	private void mockApiKeyConfigBasedOnApiKeyFromConfig(String apikey) {
		if (ObjectUtils.allNull(apikey)) {
			given(apiKeyConfig.getApikey()).willReturn(null);
		} else if (StringUtils.isEmpty(apikey)) {
			given(apiKeyConfig.getApikey()).willReturn(StringUtils.EMPTY);
		}
	}
}