package nl.craftsmen.contact.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FieldnamesTest {

    @Test
    @DisplayName("When calling getFieldNames, I expect an array of fieldnames to be returned")
    void getFieldnamesExpectAnArrayOfFieldnames() {
        // GIVEN / WHEN
        final var fieldnames = Fieldnames.getFieldnames();

        // THEN
        assertThat(fieldnames)
                .containsExactlyInAnyOrder(
                        "contactRecord",
                        "firstName",
                        "lastName",
                        "address1",
                        "address2",
                        "address3",
                        "zipcode",
                        "city",
                        "state",
                        "phone",
                        "email",
                        "iban",
                        "socialSecurityNumber",
                        "death",
                        "birth");
    }
}