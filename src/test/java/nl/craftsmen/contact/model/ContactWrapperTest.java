package nl.craftsmen.contact.model;

import static nl.craftsmen.ContactsTestdataSupplier.createContact;
import static org.assertj.core.api.Assertions.assertThat;
import javax.validation.Validator;
import nl.craftsmen.validation.ValidatorUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContactWrapperTest {

	private static Validator validator;

	@BeforeAll
	static void setup() {
		validator = ValidatorUtil.getValidator();
	}

	@Test
	@DisplayName("Given a ContactWrapper where all fields are valid, I expect no errors when validation is called")
	void constructContactWrapperExpectNoConstraintViolations() {
		// GIVEN
		final var contact = createContact();
		final var contactWrapper = ContactWrapper.builder()
				.contactRecord(contact.toString())
				.contact(contact)
				.build();

		// WHEN
		final var violations = validator.validate(contactWrapper);

		// THEN
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("Given a ContactWrapper without required fields, I expect 2 errors when validation is called")
	void constructContactWrapperWithoutRequiredFieldsResultsInConstraintViolations() {
		// GIVEN
		final var contactWrapper = ContactWrapper.builder().build();

		// WHEN
		final var violations = validator.validate(contactWrapper);

		// THEN
		assertThat(violations)
				.hasSize(2)
				.map(p -> p.getPropertyPath().toString())
				.contains("contactRecord", "contact");
	}
}