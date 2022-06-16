package nl.craftsmen.contact.client.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.BDDMockito.given;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class ContactsWebClientConfigTest {

	@Mock
	private ContactsClientConfigProps contactsClientConfigProps;

	private ContactsWebClientConfig cut = new ContactsWebClientConfig();

	@Test
	@DisplayName("When calling contactsWebClient with contactsClientConfigProps, I expect a WebClient to be returned")
	void contactsWebClient() {
		// GIVEN
		given(contactsClientConfigProps.getApikey()).willReturn("dummyApikey");

		// WHEN
		final var result = cut.contactsWebClient(contactsClientConfigProps);

		// THEN
		final var defaultHeaders = field("defaultHeaders").ofType(HttpHeaders.class).in(result).get();
		assertThat(result).isInstanceOf(WebClient.class);
		assertThat(defaultHeaders)
				.extracting(
						h -> h.get("Accept"),
						h -> h.get("Content-Type"),
						h -> h.get("apikey"))
				.containsExactly(
						List.of(MediaType.APPLICATION_JSON.toString()),
						List.of(MediaType.APPLICATION_JSON.toString()),
						List.of("dummyApikey")
				);
	}
}
