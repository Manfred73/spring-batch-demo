package nl.craftsmen.contact.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.batch.item.file.transform.Range;

class ContactDetailTest {

    @ParameterizedTest(name = "{index}: enumtype {0}, fromPosition {1}, toPosition {2}")
    @MethodSource("enumRangeGenerator")
    @DisplayName("Given an enumType, when calling getRange, I expect the correct start and end position to be returned")
    void getFromPosition(ContactDetail enumType, int fromPosition, int toPosition) {
        assertThat(enumType.getRange()).extracting(Range::getMin, Range::getMax).containsExactly(fromPosition, toPosition);
    }

    private static Stream<Arguments> enumRangeGenerator() {
        return Stream.of(
                Arguments.of(ContactDetail.CONTACT_RECORD, 1, 380),
                Arguments.of(ContactDetail.FIRST_NAME, 1, 30),
                Arguments.of(ContactDetail.LAST_NAME, 31, 60),
                Arguments.of(ContactDetail.ADDRESS_1, 61, 90),
                Arguments.of(ContactDetail.ADDRESS_2, 91, 120),
                Arguments.of(ContactDetail.ADDRESS_3, 121, 150),
                Arguments.of(ContactDetail.ZIPCODE, 151, 170),
                Arguments.of(ContactDetail.CITY, 171, 200),
                Arguments.of(ContactDetail.STATE, 201, 230),
                Arguments.of(ContactDetail.PHONE, 231, 255),
                Arguments.of(ContactDetail.EMAIL, 256, 305),
                Arguments.of(ContactDetail.IBAN, 306, 339),
                Arguments.of(ContactDetail.SOCIAL_SECURITY_NUMBER, 340, 364),
                Arguments.of(ContactDetail.DATE_OF_DEATH, 365, 372),
                Arguments.of(ContactDetail.DATE_OF_BIRTH, 373, 380));
    }
}