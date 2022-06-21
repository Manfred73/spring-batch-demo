package nl.craftsmen.contact.job.reader;

import lombok.AllArgsConstructor;
import nl.craftsmen.contact.model.ContactWrapper;
import nl.craftsmen.contact.model.ContactDetail;
import nl.craftsmen.contact.model.Fieldnames;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@AllArgsConstructor
public class ProcessContactsFileReaderConfig {

    private final SkipRecordCallback skipRecordCallback;
    private final LineTokenizer lineTokenizer;
    private final ContactsFileRowMapper contactsFileRowMapper;

    @Bean
    @StepScope
    public ItemStreamReader<ContactWrapper> contactReader(
            @Value("#{jobParameters[filename]}") final String filename) {
        final var lineMapper = createLineMapper(lineTokenizer);
        return createReader(lineMapper, filename);
    }

    private LineMapper<ContactWrapper> createLineMapper(LineTokenizer lineTokenizer) {
        final var mapper = new DefaultLineMapper<ContactWrapper>();
        mapper.setLineTokenizer(lineTokenizer);
        mapper.setFieldSetMapper(contactsFileRowMapper);
        return mapper;
    }

    private ItemStreamReader<ContactWrapper> createReader(LineMapper<ContactWrapper> lineMapper, String filename) {
        FlatFileItemReader<ContactWrapper> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(filename));
        reader.setLinesToSkip(1);
        reader.setSkippedLinesCallback(skipRecordCallback);
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
