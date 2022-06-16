package nl.craftsmen.contact.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "contactsclient")
@Getter
@Setter
public class ContactsClientConfigProps {
	private String baseUrl;
	private String apikey;
}
