package nl.craftsmen.contact.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import nl.craftsmen.contact.model.Contact;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ContactsClientTest {

    @Mock
    private WebClient contactWebClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private Contact contactPayload;

    @Mock
    private Contact contactResponse;

    @Captor
    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<Mono> bodyPayloadArgumentCaptor;

    @InjectMocks
    private ContactsClient cut;

    @Test
    @DisplayName("Given a payload, I expect this payload is provided when calling the ContactsClient.postContact and " +
            "that a Contact is being returned")
    @SuppressWarnings("unchecked")
    void postContact() {
        // GIVEN
        final var contactResponseMono = Mono.just(contactResponse);
        given(contactWebClient.post()).willReturn(requestBodyUriSpec);
        given(requestBodyUriSpec.body(any(), eq(Contact.class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.bodyToMono(Contact.class)).willReturn(contactResponseMono);

        // WHEN
        final var result = cut.postContact(contactPayload);

        // THEN
        StepVerifier.create(result)
                .assertNext(r -> assertThat(r).isEqualTo(contactResponse))
                .verifyComplete();

        verify(requestBodyUriSpec).body(bodyPayloadArgumentCaptor.capture(), eq(Contact.class));
        final var bodyPayload = bodyPayloadArgumentCaptor.getValue();
        assertThat(bodyPayload.block()).isEqualTo(contactPayload);
    }
}