package nl.craftsmen.contact.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessContactsAsyncConfig {

	@Bean
	public int asyncThreadCorePoolSize(ProcessContactsAsyncConfigProps props) {
		return props.getCorePoolSize();
	}

	@Bean
	public int asyncThreadMaxPoolSize(ProcessContactsAsyncConfigProps props) {
		return props.getMaxPoolSize();
	}

	@Bean
	public int asyncThreadQueueCapacity(ProcessContactsAsyncConfigProps props) {
		return props.getQueueCapacity();
	}
}
