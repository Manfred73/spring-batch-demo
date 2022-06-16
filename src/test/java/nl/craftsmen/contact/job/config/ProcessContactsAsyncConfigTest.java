package nl.craftsmen.contact.job.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProcessContactsAsyncConfigTest {

	@Test
	@DisplayName("Given a thread pool size of 20, when creating the properties and calling asyncThreadCorePoolSize, " +
			"I expect 20 to be returned")
	void asyncThreadCorePoolSizeExpectToBe20() {
		// GIVEN
		final var corePoolSize = 20;
		final var props = ProcessContactsAsyncConfigProps.builder().corePoolSize(corePoolSize).build();

		// WHEN
		final var result = new ProcessContactsAsyncConfig().asyncThreadCorePoolSize(props);

		// THEN
		assertThat(result).isEqualTo(corePoolSize);
	}

	@Test
	@DisplayName("Given a max pool size of 20, when creating the properties and calling asyncThreadMaxPoolSize, " +
			"I expect 20 to be returned")
	void asyncThreadMaxPoolSizeExpectToBe20() {
		// GIVEN
		final var maxPoolSize = 20;
		final var props = ProcessContactsAsyncConfigProps.builder().maxPoolSize(maxPoolSize).build();

		// WHEN
		final var result = new ProcessContactsAsyncConfig().asyncThreadMaxPoolSize(props);

		// THEN
		assertThat(result).isEqualTo(maxPoolSize);
	}

	@Test
	@DisplayName("Given a queue capacity of 20, when creating the properties and calling asyncThreadQueueCapacity, " +
			"I expect 20 to be returned")
	void asyncThreadQueueCapacityExpectToBe20() {
		// GIVEN
		final var queueCapacity = 20;
		final var props = ProcessContactsAsyncConfigProps.builder().queueCapacity(queueCapacity).build();

		// WHEN
		final var result = new ProcessContactsAsyncConfig().asyncThreadQueueCapacity(props);

		// THEN
		assertThat(result).isEqualTo(queueCapacity);
	}
}