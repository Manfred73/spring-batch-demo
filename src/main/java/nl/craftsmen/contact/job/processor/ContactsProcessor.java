package nl.craftsmen.contact.job.processor;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.craftsmen.contact.client.ContactsClient;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.util.ConditionalLogger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ContactsProcessor implements ItemProcessor<ContactWrapper, ContactWrapper> {

	private final ContactsClient contactsClient;
	private final ConditionalLogger logger;

	@Override
	public ContactWrapper process(@NonNull ContactWrapper contactWrapper) {
		try {
			// Since we use Spring Batch here, we do a blocking call to the client
			contactsClient.postContact(contactWrapper.getContact()).block();
		} catch (Exception exception) {
			logger.error(log, "Error occurred while processing contact: {}",
					contactWrapper.getContactRecord(),
					exception);
		}
		return contactWrapper;
	}
}
