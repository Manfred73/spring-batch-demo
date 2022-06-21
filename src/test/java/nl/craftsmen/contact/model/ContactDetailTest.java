package nl.craftsmen.contact.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.batch.item.file.transform.Range;

class ContactDetailTest {

    @ParameterizedTest(name = "{index}: enumtype {0}, fromPosition {1}, toPosition {2}")
    @MethodSource("enumRangeGenerator")
    @DisplayName("Given an enumType, when calling getRange, I expect the correct start and end position to be returned")
    void getRange(ContactDetail enumType, int fromPosition, int toPosition) {
        assertThat(enumType.getRange()).extracting(Range::getMin, Range::getMax)
                .containsExactly(fromPosition, toPosition);
    }

    @Test
    @DisplayName("When getColumnRanges is called, I expect an array of ranges to be returned in the correct order")
    void getColumnRangesExpectAnArrayOfRanges() {
        // GIVEN / WHEN
        final var columnRanges = ContactDetail.getColumnRanges();

        final var comparator = new Comparator<Range>() {
            @Override
            public int compare(Range o1, Range o2) {
                return (o1.getMin() == o2.getMin() && o1.getMax() == o2.getMax()) ? 0 : 1;
            }
        };

        // THEN
        assertThat(columnRanges)
                .usingElementComparator(comparator)
                .containsExactly(
                        new Range(1, 380),
                        new Range(1, 30),
                        new Range(31, 60),
                        new Range(61, 90),
                        new Range(91, 120),
                        new Range(121, 150),
                        new Range(151, 170),
                        new Range(171, 200),
                        new Range(201, 230),
                        new Range(231, 255),
                        new Range(256, 305),
                        new Range(306, 339),
                        new Range(340, 364),
                        new Range(365, 372),
                        new Range(373, 380));
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