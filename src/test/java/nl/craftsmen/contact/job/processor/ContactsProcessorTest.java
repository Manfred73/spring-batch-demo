package nl.craftsmen.contact.job.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import nl.craftsmen.contact.client.ContactsClient;
import nl.craftsmen.contact.model.Contact;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.exceptionhandling.UnexpectedTestException;
import nl.craftsmen.util.ConditionalLogger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ContactsProcessorTest {

	private static final Logger LOG = LoggerFactory.getLogger(ContactsProcessor.class);

	@Mock
	private ContactsClient contactsClient;

	@Mock
	private ConditionalLogger logger;

	@InjectMocks
	private ContactsProcessor cut;

	@Test
	@DisplayName("Given a ContactWrapper, I expect that ContactsClient has been called and that ContactWrapper is " +
			"being returned")
	void processExpectContactsClientHasBeenCalledAndContactWrapperIsBeingReturned() {
		// GIVEN
		final var contact = Contact.builder().build();
		final var contactWrapper = ContactWrapper.builder()
				.contact(contact)
				.build();
		final var contactMono = Mono.just(contact);
		given(contactsClient.postContact(contactWrapper.getContact())).willReturn(contactMono);

		// WHEN
		final var result = cut.process(contactWrapper);

		// THEN
		assertThat(result).isEqualTo(contactWrapper);
		verify(contactsClient).postContact(contact);
		verifyNoMoreInteractions(contactsClient, logger);
	}

	@Test
	@DisplayName("Given a ContactWrapper, I expect that ContactsClient has been called and an exception has occurred, " +
			"which has been logged, and that ContactWrapper has been returned")
	void processExpectExceptionWhenCallingContactsClientToBeLoggedAndContactWrapperToBeReturned() {
		// GIVEN
		final var contactRecord = "This is a contact json object";
		final var contact = Contact.builder().build();
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contactRecord)
				.contact(contact)
				.build();
		final var exception = new UnexpectedTestException();
		given(contactsClient.postContact(contact)).willThrow(exception);

		// WHEN
		final var result = cut.process(contactWrapper);

		// THEN
		assertThat(result).isEqualTo(contactWrapper);
		verify(contactsClient).postContact(contact);
		verify(logger).error(LOG, "Error occurred while processing contact: {}", contactRecord, exception);
		verifyNoMoreInteractions(contactsClient, logger);
	}

	@Test
	@DisplayName("When process is being called without a ContactWrapper, I expect a NullPointerException")
	void processExpectNullPointerExceptionBecauseContactWrapperIsNull() {
		// GIVEN / WHEN
		final var thrown = catchThrowable(() -> cut.process(null));

		// THEN
		assertThat(thrown)
				.isInstanceOf(NullPointerException.class)
				.hasMessage("contactWrapper is marked non-null but is null");
	}
}