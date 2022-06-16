package nl.craftsmen.contact.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonPropertyOrder({
		Fieldnames.FIRST_NAME,
		Fieldnames.LAST_NAME,
		Fieldnames.ADDRESS_1,
		Fieldnames.ADDRESS_2,
		Fieldnames.ADDRESS_3,
		Fieldnames.ZIPCODE,
		Fieldnames.CITY,
		Fieldnames.STATE,
		Fieldnames.PHONE,
		Fieldnames.EMAIL,
		Fieldnames.IBAN,
		Fieldnames.SOCIAL_SECURITY_NUMBER,
		Fieldnames.DATE_OF_DEATH,
		Fieldnames.DATE_OF_BIRTH
})
public class Contact {

	@NotNull
	@Size(max = 30)
	private String firstName;

	@NotNull
	@Size(max = 30)
	private String lastName;

	@NotNull
	@Size(max = 30)
	private String address1;

	@Size(max = 30)
	private String address2;

	@Size(max = 30)
	private String address3;

	@NotNull
	@Size(max = 20)
	private String zipcode;

	@NotNull
	@Size(max = 30)
	private String city;

	@Size(max = 30)
	private String state;

	@Size(max = 25)
	private String phone;

	@Size(max = 50)
	private String email;

	@NotNull
	@Size(max = 34)
	private String iban;

	@Size(max = 25)
	private String socialSecurityNumber;

	private LocalDate dateOfDeath;

	@NotNull
	private LocalDate dateOfBirth;
}
