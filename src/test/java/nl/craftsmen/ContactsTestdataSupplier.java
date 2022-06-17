package nl.craftsmen;

import java.net.URI;
import java.time.LocalDate;
import java.util.function.Function;
import nl.craftsmen.contact.model.Contact;
import org.springframework.web.util.UriBuilder;

public class ContactsTestdataSupplier {

	public static final String RESOURCE_PATH = "/batchjob";

	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "A generic error has occurred";
	public static final String CONSTRAINT_VIOLATION_ERROR_MESSAGE = "ConstraintViolationException occurred while processing";
	public static final String METHOD_ARGUMENT_NOT_VALID_ERROR_MESSAGE =
			"MethodArgumentNotValidException occurred while processing";
	public static final String METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE =
			"Parameter parameterName should be of type java.lang.String";

	public static final String FIRST_NAME = "PETE";
	public static final String LAST_NAME = "PETERSON";
	public static final String ADDRESS_1 = "46 VICTORIA ROAD";
	public static final String ADDRESS_2 = "BUILDING 1B EAST";
	public static final String ADDRESS_3 = "SECOND FLOOR";
	public static final String ZIPCODE = "WC91 4XH";
	public static final String CITY = "LONDON";
	public static final String STATE = "WESTMINSTER";
	public static final String PHONE = "+44 20 7946 0620";
	public static final String EMAIL = "PETE.PETERSON@HOTMAIL.COM";
	public static final String IBAN = "GB98MIDL07009312345678";
	public static final String SOCIAL_SECURITY_NUMBER = "408-03-7262";
	public static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 4, 10);
	public static final LocalDate DATE_OF_BIRTH = LocalDate.of(1946, 1, 1);

	public static Contact createContact() {
		return Contact.builder()
				.firstName(FIRST_NAME)
				.lastName(LAST_NAME)
				.address1(ADDRESS_1)
				.address2(ADDRESS_2)
				.address3(ADDRESS_3)
				.zipcode(ZIPCODE)
				.city(CITY)
				.state(STATE)
				.phone(PHONE)
				.email(EMAIL)
				.iban(IBAN)
				.socialSecurityNumber(SOCIAL_SECURITY_NUMBER)
				.dateOfDeath(DATE_OF_DEATH)
				.dateOfBirth(DATE_OF_BIRTH)
				.build();
	}

	public static Function<UriBuilder, URI> getUriForRunningBatchjob() {
		return uriBuilder -> uriBuilder
				.path(RESOURCE_PATH + "/run")
				.build();
	}
}
