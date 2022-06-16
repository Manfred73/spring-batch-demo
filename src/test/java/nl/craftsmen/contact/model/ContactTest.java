package nl.craftsmen.contact.model;

import static nl.craftsmen.ContactsTestdataSupplier.createContact;
import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.Validator;

import nl.craftsmen.validation.ValidatorUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContactTest {

    private static final String SIZE_EXCEEDS_20 = "123456789012345678901";
    private static final String SIZE_EXCEEDS_25 = "12345678901234567890123456";
    private static final String SIZE_EXCEEDS_30 = "1234567890123456789012345678901";
    private static final String SIZE_EXCEEDS_34 = "12345678901234567890123456789012345";
    private static final String SIZE_EXCEEDS_50 = "123456789012345678901234567890123456789012345678901";

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = ValidatorUtil.getValidator();
    }

    @Test
    @DisplayName("Given a Contact with all fields valid, I do not expect any errors when validation is called")
    void constructContactExpectNoConstraintViolations() {
        // GIVEN
        final var contact = createContact();

        // WHEN
        final var violations = validator.validate(contact);

        // THEN
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Given a Contact without required fields, I expect 7 errors when validation is called")
    void constructContactWithoutRequiredFieldsResultsInConstraintViolations() {
        // GIVEN
        final var contact = Contact.builder().build();

        // WHEN
        final var violations = validator.validate(contact);

        // THEN
        assertThat(violations)
                .hasSize(7)
                .map(p -> p.getPropertyPath().toString())
                .contains("firstName", "lastName", "address1", "zipcode", "city", "iban", "dateOfBirth");
    }

    @Test
    @DisplayName("Given a Contact where the length of certain fields is exceeded, I expect 1 error when validation is "
            + "called")
    void constructContactSizeExceededResultsInConstraintViolations() {
        // GIVEN
        final var contact = createContact().toBuilder()
                .firstName(SIZE_EXCEEDS_30)
                .lastName(SIZE_EXCEEDS_30)
                .address1(SIZE_EXCEEDS_30)
                .address2(SIZE_EXCEEDS_30)
                .address3(SIZE_EXCEEDS_30)
                .zipcode(SIZE_EXCEEDS_20)
                .city(SIZE_EXCEEDS_30)
                .state(SIZE_EXCEEDS_30)
                .phone(SIZE_EXCEEDS_25)
                .email(SIZE_EXCEEDS_50)
                .iban(SIZE_EXCEEDS_34)
                .socialSecurityNumber(SIZE_EXCEEDS_25)
                .build();

        // WHEN
        final var violations = validator.validate(contact);

        // THEN
        assertThat(violations)
                .hasSize(12)
                .map(p -> p.getPropertyPath().toString())
                .contains("firstName", "lastName", "address1", "address2", "address3", "zipcode", "city", "state",
                        "phone", "email", "iban", "socialSecurityNumber");
    }
}