package nl.craftsmen.contact.job.reader;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import nl.craftsmen.contact.model.Fieldnames;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;

class ContactsFileRowMapperTest {

    private static final String FIRST_NAME = "PETE                          ";
    private static final String LAST_NAME = "PETERSON                      ";
    private static final String ADDRESS_1 = "46 VICTORIA ROAD              ";
    private static final String ADDRESS_2 = "BUILDING 1B EAST              ";
    private static final String ADDRESS_2_EMPTY = StringUtils.EMPTY;
    private static final String ADDRESS_3 = "SECOND FLOOR                  ";
    private static final String ADDRESS_3_EMPTY = StringUtils.EMPTY;
    private static final String ZIPCODE = "WC91 4XH            ";
    private static final String CITY = "LONDON                        ";
    private static final String STATE = "WESTMINSTER                   ";
    private static final String STATE_EMPTY = StringUtils.EMPTY;
    private static final String PHONE = "+44 20 7946 0620         ";
    private static final String PHONE_EMPTY = StringUtils.EMPTY;
    private static final String EMAIL = "PETE.PETERSON@HOTMAIL.COM                         ";
    private static final String EMAIL_EMPTY = StringUtils.EMPTY;
    private static final String IBAN = "GB98MIDL07009312345678            ";
    private static final String SOCIAL_SECURITY_NUMBER = "408-03-7262              ";
    private static final String SOCIAL_SECURITY_NUMBER_EMPTY = StringUtils.EMPTY;
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 4, 10);
    private static final String DATE_OF_DEATH_STRING = "10042018";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1946, 1, 1);
    private static final String DATE_OF_BIRTH_STRING = "01011946";
    private static final String DATE_OF_DEATH_STRING_EMPTY = StringUtils.EMPTY;

    private final ContactsFileRowMapper cut = new ContactsFileRowMapper();

    @Test
    @DisplayName("Given a FieldSet of names and tokens, I expect a ContactWrapper to be returned with the correct " +
            "values de when the mapFieldSet method has been called")
    void mapFieldSetExpectContactWrapperToBeReturnedWithCorrectValues() {
        // GIVEN
        final var fieldnames = Fieldnames.getFieldnames();
        final var fieldtokens = getFieldtokens();
        final var fieldSet = new DefaultFieldSet(fieldtokens, fieldnames);

        // WHEN
        final var result = cut.mapFieldSet(fieldSet);

        // THEN
        assertThat(result)
                .extracting(
                        aw -> result.getContactRecord(),
                        aw -> result.getContact().getFirstName(),
                        aw -> result.getContact().getLastName(),
                        aw -> result.getContact().getAddress1(),
                        aw -> result.getContact().getAddress2(),
                        aw -> result.getContact().getAddress3(),
                        aw -> result.getContact().getZipcode(),
                        aw -> result.getContact().getCity(),
                        aw -> result.getContact().getState(),
                        aw -> result.getContact().getPhone(),
                        aw -> result.getContact().getEmail(),
                        aw -> result.getContact().getIban(),
                        aw -> result.getContact().getSocialSecurityNumber(),
                        aw -> result.getContact().getDateOfDeath(),
                        aw -> result.getContact().getDateOfBirth())
                .containsExactly(
                        getContactRecord(),
                        FIRST_NAME.trim(),
                        LAST_NAME.trim(),
                        ADDRESS_1.trim(),
                        ADDRESS_2.trim(),
                        ADDRESS_3.trim(),
                        ZIPCODE.trim(),
                        CITY.trim(),
                        STATE.trim(),
                        PHONE.trim(),
                        EMAIL.trim(),
                        IBAN.trim(),
                        SOCIAL_SECURITY_NUMBER.trim(),
                        DATE_OF_DEATH,
                        DATE_OF_BIRTH);
    }

    @Test
    @DisplayName("Given a FieldSet with names and tokens of which several values are empty, I expect a ContactWrapper "
            + "to be returned with the correct values when the mapFieldSet method has been called")
    void mapFieldSetExpectContactWrapperWithCorrectValuesToBeReturnedOfWhichSeveralValuesAreNull() {
        // GIVEN
        final var fieldnames = Fieldnames.getFieldnames();
        final var fieldtokens = getFieldtokensWithEmptyFields();
        final var fieldSet = new DefaultFieldSet(fieldtokens, fieldnames);

        // WHEN
        final var result = cut.mapFieldSet(fieldSet);

        // THEN
        assertThat(result)
                .extracting(
                        aw -> result.getContactRecord(),
                        aw -> result.getContact().getFirstName(),
                        aw -> result.getContact().getLastName(),
                        aw -> result.getContact().getAddress1(),
                        aw -> result.getContact().getAddress2(),
                        aw -> result.getContact().getAddress3(),
                        aw -> result.getContact().getZipcode(),
                        aw -> result.getContact().getCity(),
                        aw -> result.getContact().getState(),
                        aw -> result.getContact().getPhone(),
                        aw -> result.getContact().getEmail(),
                        aw -> result.getContact().getIban(),
                        aw -> result.getContact().getSocialSecurityNumber(),
                        aw -> result.getContact().getDateOfDeath(),
                        aw -> result.getContact().getDateOfBirth())
                .containsExactly(
                        getContactRecordWithEmptyFields(),
                        FIRST_NAME.trim(),
                        LAST_NAME.trim(),
                        ADDRESS_1.trim(),
                        null,
                        null,
                        ZIPCODE.trim(),
                        CITY.trim(),
                        null,
                        null,
                        null,
                        IBAN.trim(),
                        null,
                        null,
                        DATE_OF_BIRTH);
    }

    private String[] getFieldtokens() {
        return new String[]{
                getContactRecord(), FIRST_NAME, LAST_NAME, ADDRESS_1, ADDRESS_2, ADDRESS_3, ZIPCODE, CITY, STATE,
                PHONE, EMAIL, IBAN, SOCIAL_SECURITY_NUMBER, DATE_OF_DEATH_STRING, DATE_OF_BIRTH_STRING};

    }

    private String[] getFieldtokensWithEmptyFields() {

        return new String[]{
                getContactRecordWithEmptyFields(), FIRST_NAME, LAST_NAME, ADDRESS_1, ADDRESS_2_EMPTY, ADDRESS_3_EMPTY,
                ZIPCODE, CITY, STATE_EMPTY, PHONE_EMPTY, EMAIL_EMPTY, IBAN, SOCIAL_SECURITY_NUMBER_EMPTY,
                DATE_OF_DEATH_STRING_EMPTY, DATE_OF_BIRTH_STRING};
    }

    private String getContactRecord() {
        return FIRST_NAME
                + LAST_NAME
                + ADDRESS_1
                + ADDRESS_2
                + ADDRESS_3
                + ZIPCODE
                + CITY
                + STATE
                + PHONE
                + EMAIL
                + IBAN
                + SOCIAL_SECURITY_NUMBER
                + DATE_OF_DEATH_STRING
                + DATE_OF_BIRTH_STRING;
    }

    private String getContactRecordWithEmptyFields() {
        return FIRST_NAME
                + LAST_NAME
                + ADDRESS_1
                + ADDRESS_2_EMPTY
                + ADDRESS_3_EMPTY
                + ZIPCODE
                + CITY
                + STATE_EMPTY
                + PHONE_EMPTY
                + EMAIL_EMPTY
                + IBAN
                + SOCIAL_SECURITY_NUMBER_EMPTY
                + DATE_OF_DEATH_STRING_EMPTY
                + DATE_OF_BIRTH_STRING;
    }
}