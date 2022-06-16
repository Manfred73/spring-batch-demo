package nl.craftsmen.contact.job.writer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;

@ExtendWith(MockitoExtension.class)
class AsyncContactsWriterConfigTest {

	@Mock
	private NoOpItemWriter noOpItemWriter;

	@Test
	@DisplayName("When getAsyncItemWriter has been called, I expect a configured ItemWriter to be returned")
	void getAsyncItemWriterExpectAConfiguredItemWriterToBeReturned() {
		// GIVEN
		final var cut = new AsyncContactsWriterConfig(noOpItemWriter);

		// WHEN
		final var result = cut.getAsyncItemWriter();

		// THEN
		final var delegate = field("delegate").ofType(ItemWriter.class).in(result).get();
		assertThat(result).isInstanceOf(AsyncItemWriter.class);
		assertThat(delegate).isEqualTo(noOpItemWriter);
	}
}