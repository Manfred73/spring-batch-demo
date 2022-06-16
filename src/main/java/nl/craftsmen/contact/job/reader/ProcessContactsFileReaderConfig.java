package nl.craftsmen.contact.job.reader;

import lombok.AllArgsConstructor;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.contact.model.ContactDetail;
import nl.craftsmen.contact.model.Fieldnames;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@AllArgsConstructor
public class ProcessContactsFileReaderConfig {

    private final SkipRecordCallback skipRecordCallback;
    private final ContactsFileRowMapper contactsFileRowMapper;

    @Bean
    @StepScope
    public FlatFileItemReader<ContactWrapper> contactReader(
            @Value("#{jobParameters[filename]}") final String filename) {
        final var lineTokenizer = createLineTokenizer();
        final var lineMapper = createLineMapper(lineTokenizer);
        return createReader(lineMapper, filename);
    }

    private FixedLengthTokenizer createLineTokenizer() {
        final var lineTokenizer = new FixedLengthTokenizer();
        lineTokenizer.setNames(Fieldnames.getFieldnames());
        lineTokenizer.setColumns(createColumns());
        lineTokenizer.setStrict(false);
        return lineTokenizer;
    }

    private DefaultLineMapper<ContactWrapper> createLineMapper(FixedLengthTokenizer lineTokenizer) {
        final var mapper = new DefaultLineMapper<ContactWrapper>();
        mapper.setLineTokenizer(lineTokenizer);
        mapper.setFieldSetMapper(contactsFileRowMapper);
        return mapper;
    }

    private Range[] createColumns() {
        return new Range[]{
                ContactDetail.CONTACT_RECORD.getRange(),
                ContactDetail.FIRST_NAME.getRange(),
                ContactDetail.LAST_NAME.getRange(),
                ContactDetail.ADDRESS_1.getRange(),
                ContactDetail.ADDRESS_2.getRange(),
                ContactDetail.ADDRESS_3.getRange(),
                ContactDetail.ZIPCODE.getRange(),
                ContactDetail.CITY.getRange(),
                ContactDetail.STATE.getRange(),
                ContactDetail.PHONE.getRange(),
                ContactDetail.EMAIL.getRange(),
                ContactDetail.IBAN.getRange(),
                ContactDetail.SOCIAL_SECURITY_NUMBER.getRange(),
                ContactDetail.DATE_OF_DEATH.getRange(),
                ContactDetail.DATE_OF_BIRTH.getRange()};
    }

    private FlatFileItemReader<ContactWrapper> createReader(DefaultLineMapper<ContactWrapper> lineMapper, String filename) {
        FlatFileItemReader<ContactWrapper> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(filename));
        reader.setLinesToSkip(1);
        reader.setSkippedLinesCallback(skipRecordCallback);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
