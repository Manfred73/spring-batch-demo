package nl.craftsmen.contact.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessContactsConfig {

	@Bean
	public int chunkSize(ProcessContactsConfigProps props) {
		return props.getChunkSize();
	}
}
