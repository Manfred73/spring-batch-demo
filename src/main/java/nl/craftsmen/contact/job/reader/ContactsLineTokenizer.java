package nl.craftsmen.contact.job.reader;

import nl.craftsmen.contact.model.ContactDetail;
import nl.craftsmen.contact.model.Fieldnames;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactsLineTokenizer {

    @Bean
    public FixedLengthTokenizer createLineTokenizer() {
        final var lineTokenizer = new FixedLengthTokenizer();
        lineTokenizer.setNames(Fieldnames.getFieldnames());
        lineTokenizer.setColumns(ContactDetail.getColumnRanges());
        lineTokenizer.setStrict(false);
        return lineTokenizer;
    }
}
