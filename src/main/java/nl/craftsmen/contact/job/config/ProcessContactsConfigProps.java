package nl.craftsmen.contact.job.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "processingcontactsjob")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessContactsConfigProps {
	private int chunkSize;
}
