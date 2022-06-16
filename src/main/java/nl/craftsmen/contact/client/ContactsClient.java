package nl.craftsmen.contact.client;

import lombok.extern.slf4j.Slf4j;
import nl.craftsmen.contact.model.Contact;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ContactsClient {
    private final WebClient contactsWebClient;

    public ContactsClient(@Qualifier("contactsWebClient") WebClient contactsWebClient) {
        this.contactsWebClient = contactsWebClient;
    }

    public Mono<Contact> postContact(Contact contact) {
        log.info(">>>>> Sending payload to Contacts.");
        return contactsWebClient.post()
                .body(Mono.just(contact), Contact.class)
                .retrieve()
                .bodyToMono(Contact.class);
    }
}
