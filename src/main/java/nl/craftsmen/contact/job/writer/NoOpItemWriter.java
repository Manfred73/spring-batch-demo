package nl.craftsmen.contact.job.writer;

import java.util.List;
import lombok.NonNull;
import nl.craftsmen.contact.model.ContactWrapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class NoOpItemWriter implements ItemWriter<ContactWrapper> {

	@Override
	public void write(@NonNull List<? extends ContactWrapper> list) {
		// no-op
	}
}
