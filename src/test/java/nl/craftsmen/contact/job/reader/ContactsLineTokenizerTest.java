package nl.craftsmen.contact.job.reader;

import nl.craftsmen.contact.model.Fieldnames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.transform.Range;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;

class ContactsLineTokenizerTest {

    @Test
    @DisplayName("When createLineTokenizer has been called, I expect a configured FixedLengthTokenizer to be returned")
    void createLineTokenizerExpectAConfiguredFixedLengthTokenizerToBeReturned() {
        // GIVEN
        final var cut = new ContactsLineTokenizer();

        // WHEN
        final var result = cut.createLineTokenizer();

        // THEN
        final var lineTokenizerNames = field("names").ofType(String[].class).in(result).get();
        final var lineTokenizerColumns = field("ranges").ofType(Range[].class).in(result).get();
        final var lineTokenizerIsStrict = field("strict").ofType(boolean.class).in(result).get();
        assertThat(lineTokenizerNames).isEqualTo(Fieldnames.getFieldnames());
        assertThat(lineTokenizerColumns).isNotEmpty();
        assertThat(lineTokenizerIsStrict).isFalse();
    }
}