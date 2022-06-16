package nl.craftsmen.security.apikey;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
@Setter
@Getter
public class ApiKeyConfig {
	private String apikey;
}
