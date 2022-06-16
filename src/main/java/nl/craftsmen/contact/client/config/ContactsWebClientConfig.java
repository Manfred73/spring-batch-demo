package nl.craftsmen.contact.client.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ContactsWebClientConfig {
	private static final String HEADER_NAME_APIKEY = "apikey";

	private void addHttpHeaders(HttpHeaders httpHeaders,
			ContactsClientConfigProps contactsClientConfigProps) {
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set(HEADER_NAME_APIKEY, contactsClientConfigProps.getApikey());
	}

	@Bean
	public WebClient contactsWebClient(ContactsClientConfigProps contactsClientConfigProps) {
		log.info(">>>>> Configuring {} with url {}", "ContactsClient", contactsClientConfigProps.getBaseUrl());
		return WebClient.builder()
				.baseUrl(contactsClientConfigProps.getBaseUrl())
				.defaultHeaders(httpHeaders -> addHttpHeaders(httpHeaders, contactsClientConfigProps))
				.build();
	}
}
