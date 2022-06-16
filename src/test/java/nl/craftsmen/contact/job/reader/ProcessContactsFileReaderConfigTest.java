package nl.craftsmen.contact.job.reader;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.fest.reflect.core.Reflection.field;
import nl.craftsmen.contact.model.Fieldnames;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class ProcessContactsFileReaderConfigTest {

	@Mock
	private SkipRecordCallback skipRecordCallback;

	@Mock
	private ContactsFileRowMapper contactsFileRowMapper;

	@Test
	@DisplayName("When contactReader has been called, I expect a configured ContactReader to be returned")
	void contactReaderExpectAGeconfiguredReaderToBeReturned() {
		// GIVEN
		final var filename = "testfile.txt";
		final var cut = new ProcessContactsFileReaderConfig(skipRecordCallback, contactsFileRowMapper);

		// WHEN
		final var reader = cut.contactReader(filename);

		// THEN
		final var resource = field("resource").ofType(Resource.class).in(reader).get();
		final var linesToSkip = field("linesToSkip").ofType(int.class).in(reader).get();
		final var skippedLinesCallback = field("skippedLinesCallback").ofType(LineCallbackHandler.class).in(reader).get();
		final var lineMapper = field("lineMapper").ofType(LineMapper.class).in(reader).get();
		final var fieldSetMapper = field("fieldSetMapper").ofType(FieldSetMapper.class).in(lineMapper).get();
		final var lineTokenizer = field("tokenizer").ofType(LineTokenizer.class).in(lineMapper).get();
		final var lineTokenizerNames = field("names").ofType(String[].class).in(lineTokenizer).get();
		final var lineTokenizerColumns = field("ranges").ofType(Range[].class).in(lineTokenizer).get();
		final var lineTokenizerIsStrict = field("strict").ofType(boolean.class).in(lineTokenizer).get();
		assertThat(reader).isInstanceOf(FlatFileItemReader.class);
		assertThat(resource.getFilename()).isEqualTo(filename);
		assertThat(linesToSkip).isEqualTo(1);
		assertThat(skippedLinesCallback).isEqualTo(skipRecordCallback);
		assertThat(lineMapper).isNotNull();
		assertThat(lineTokenizer).isNotNull();
		assertThat(fieldSetMapper).isEqualTo(contactsFileRowMapper);
		assertThat(lineTokenizerNames).isEqualTo(Fieldnames.getFieldnames());
		assertThat(lineTokenizerColumns).isNotEmpty();
		assertThat(lineTokenizerIsStrict).isFalse();
	}
}