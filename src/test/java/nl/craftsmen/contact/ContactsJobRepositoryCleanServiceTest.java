package nl.craftsmen.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import nl.craftsmen.util.SpringJdbcScriptUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class ContactsJobRepositoryCleanServiceTest {

	@Mock
	private DataSource batchDataSource;

	@Mock
	private SpringJdbcScriptUtils springJdbcScriptUtils;

	@Mock
	private Connection connection;

	@Captor
	private ArgumentCaptor<Resource> resourceArgumentCaptor;

	@InjectMocks
	private ContactsJobRepositoryCleanService cut;

	@Test
	@DisplayName("Given a batchjob datasource connection, when calling removeJobExecutions, I do not expect any "
			+ "exceptions and I expect that springJdbcScriptUtils.executeSqlScript with a delete script has been "
			+ "called")
	void removeJobExecutions() throws SQLException {
		// GIVEN
		given(batchDataSource.getConnection()).willReturn(connection);

		// WHEN / THEN
		assertThatCode(() -> cut.removeJobExecutions()).doesNotThrowAnyException();

		verify(springJdbcScriptUtils).executeSqlScript(eq(connection), resourceArgumentCaptor.capture());
		final var resource = resourceArgumentCaptor.getValue();
		assertThat(resource.getFilename()).isEqualTo("schema-delete-spring-batch-metadata.sql");
	}
}