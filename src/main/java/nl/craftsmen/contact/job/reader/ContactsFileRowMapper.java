package nl.craftsmen.contact.job.reader;

import lombok.NonNull;
import nl.craftsmen.contact.model.Contact;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.contact.model.Fieldnames;
import nl.craftsmen.util.DateUtil;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContactsFileRowMapper implements FieldSetMapper<ContactWrapper> {

    @Override
    public @NonNull ContactWrapper mapFieldSet(FieldSet fieldSet) {
        return ContactWrapper.builder()
                .contactRecord(fieldSet.readString(Fieldnames.CONTACT_RECORD))
                .contact(Contact.builder()
                        .firstName(fieldSet.readString(Fieldnames.FIRST_NAME))
                        .lastName(fieldSet.readString(Fieldnames.LAST_NAME))
                        .address1(fieldSet.readString(Fieldnames.ADDRESS_1))
                        .address2(determineEmptyField(fieldSet.readString(Fieldnames.ADDRESS_2)))
                        .address3(determineEmptyField(fieldSet.readString(Fieldnames.ADDRESS_3)))
                        .zipcode(fieldSet.readString(Fieldnames.ZIPCODE))
                        .city(fieldSet.readString(Fieldnames.CITY))
                        .state(determineEmptyField(fieldSet.readString(Fieldnames.STATE)))
                        .phone(determineEmptyField(fieldSet.readString(Fieldnames.PHONE)))
                        .email(determineEmptyField(fieldSet.readString(Fieldnames.EMAIL)))
                        .iban(determineEmptyField(fieldSet.readString(Fieldnames.IBAN)))
                        .socialSecurityNumber(determineEmptyField(fieldSet.readString(Fieldnames.SOCIAL_SECURITY_NUMBER)))
                        .dateOfDeath(getDate(fieldSet.readString(Fieldnames.DATE_OF_DEATH)))
                        .dateOfBirth(getDate(fieldSet.readString(Fieldnames.DATE_OF_BIRTH)))
                        .build())
                .build();
    }

    private String determineEmptyField(String string) {
        return string.isEmpty() ? null : string;
    }

    private LocalDate getDate(String dateString) {
        final var newDateString = determineEmptyField(dateString);
        return newDateString != null ? DateUtil.parseLocalDate(newDateString, DateUtil.FORMAT_DATE_DDMMYYYY) : null;
    }
}
