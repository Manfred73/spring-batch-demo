package nl.craftsmen.util;

import java.sql.Connection;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

@Component
public class SpringJdbcScriptUtils {

	//NOSONAR we do not test static Spring methods
	public void executeSqlScript(Connection connection, Resource sqlScript) {
		ScriptUtils.executeSqlScript(connection, sqlScript);
	}
}
