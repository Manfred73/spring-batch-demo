package nl.craftsmen.contact;

import java.sql.SQLException;
import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import nl.craftsmen.util.SpringJdbcScriptUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * This service is used for test purposes, for cleaning batch job (metadata) repository before running an end-to-end test.
 */
@Service
@AllArgsConstructor
public class ContactsJobRepositoryCleanService {

    private DataSource batchDataSource;
    private SpringJdbcScriptUtils springJdbcScriptUtils;

    public void removeJobExecutions() throws SQLException {
        final var sqlScript = new ClassPathResource("schema-delete-spring-batch-metadata.sql");
        springJdbcScriptUtils.executeSqlScript(batchDataSource.getConnection(), sqlScript);
    }
}
