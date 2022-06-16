package nl.craftsmen.util;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.craftsmen.exceptionhandling.UnexpectedTestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class ConditionalLoggerTest {

	private static final String OBJECT_TYPE = "MESSAGE";
	private static final Long ID = 99L;
	private static final String ERROR_MESSAGE = "an error message";
	private static final String ERROR_MESSAGE_WITH_ARGUMENTS = "an error message with objectType {} and id {}";
	private static final String ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS = "an error message with objectType {0} and id {1}";
	private static final String FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS = "an error message with objectType MESSAGE and id 99";
	private static final Exception EXCEPTION = new UnexpectedTestException();

	@Mock
	private Logger logger;

	private ConditionalLogger cut = new ConditionalLogger();

	@Test
	void errorNotEnabledErrorMessage() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(false);

		// WHEN
		cut.error(logger, ERROR_MESSAGE);

		// THEN
		verify(logger, never()).error(ERROR_MESSAGE);
	}

	@Test
	void errorEnabledErrorMessage() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE);

		// THEN
		verify(logger).error(ERROR_MESSAGE);
	}

	@Test
	void errorNotEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(false);

		// WHEN
		cut.error(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger, never()).error(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void errorEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger).error(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void errorNotEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(false);

		// WHEN
		cut.error(logger, ERROR_MESSAGE, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).error(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void errorEnabledErrorMessageWthArguments() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE_WITH_ARGUMENTS, OBJECT_TYPE, ID);

		// THEN
		verify(logger).error(ERROR_MESSAGE_WITH_ARGUMENTS, new Object[]{ OBJECT_TYPE, ID });
	}

	@Test
	void errorNotEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(false);

		// WHEN
		cut.error(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).error(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void errorEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).error(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void errorEnabledErrorMessageWithoutArgumentsAndThrowableButArgumentsAreProvided() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).error(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void errorNotEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(false);

		// WHEN
		cut.error(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).error(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void errorEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isErrorEnabled()).thenReturn(true);

		// WHEN
		cut.error(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).error(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void warnNotEnabledErrorMessage() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(false);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE);

		// THEN
		verify(logger, never()).warn(ERROR_MESSAGE);
	}

	@Test
	void warnEnabledErrorMessage() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(true);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE);

		// THEN
		verify(logger).warn(ERROR_MESSAGE);
	}

	@Test
	void warnNotEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(false);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger, never()).warn(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void warnEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(true);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger).warn(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void warnNotEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(false);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).warn(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void warnEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(true);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE_WITH_ARGUMENTS, OBJECT_TYPE, ID);

		// THEN
		verify(logger).warn(ERROR_MESSAGE_WITH_ARGUMENTS, new Object[]{ OBJECT_TYPE, ID });
	}

	@Test
	void warnNotEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(false);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).warn(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void warnEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(true);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).warn(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void warnNotEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(false);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).warn(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void warnEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isWarnEnabled()).thenReturn(true);

		// WHEN
		cut.warn(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).warn(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void infoNotEnabledErrorMessage() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(false);

		// WHEN
		cut.info(logger, ERROR_MESSAGE);

		// THEN
		verify(logger, never()).info(ERROR_MESSAGE);
	}

	@Test
	void infoEnabledErrorMessage() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(true);

		// WHEN
		cut.info(logger, ERROR_MESSAGE);

		// THEN
		verify(logger).info(ERROR_MESSAGE);
	}

	@Test
	void infoNotEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(false);

		// WHEN
		cut.info(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger, never()).info(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void infoEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(true);

		// WHEN
		cut.info(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger).info(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void infoNotEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(false);

		// WHEN
		cut.info(logger, ERROR_MESSAGE, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).info(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void infoEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(true);

		// WHEN
		cut.info(logger, ERROR_MESSAGE_WITH_ARGUMENTS, OBJECT_TYPE, ID);

		// THEN
		verify(logger).info(ERROR_MESSAGE_WITH_ARGUMENTS, new Object[]{ OBJECT_TYPE, ID });
	}

	@Test
	void infoNotEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(false);

		// WHEN
		cut.info(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).info(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void infoEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(true);

		// WHEN
		cut.info(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).info(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void infoNotEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(false);

		// WHEN
		cut.info(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).info(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void infoEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isInfoEnabled()).thenReturn(true);

		// WHEN
		cut.info(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).info(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void debugNotEnabledErrorMessage() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(false);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE);

		// THEN
		verify(logger, never()).debug(ERROR_MESSAGE);
	}

	@Test
	void debugEnabledErrorMessage() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(true);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE);

		// THEN
		verify(logger).debug(ERROR_MESSAGE);
	}

	@Test
	void debugNotEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(false);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger, never()).debug(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void debugEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(true);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger).debug(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void debugNotEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(false);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).debug(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void debugEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(true);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE_WITH_ARGUMENTS, OBJECT_TYPE, ID);

		// THEN
		verify(logger).debug(ERROR_MESSAGE_WITH_ARGUMENTS, new Object[]{ OBJECT_TYPE, ID });
	}

	@Test
	void debugNotEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(false);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).debug(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void debugEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(true);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).debug(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void debugNotEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(false);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).debug(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void debugEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isDebugEnabled()).thenReturn(true);

		// WHEN
		cut.debug(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).debug(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void traceNotEnabledErrorMessage() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(false);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE);

		// THEN
		verify(logger, never()).trace(ERROR_MESSAGE);
	}

	@Test
	void traceEnabledErrorMessage() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(true);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE);

		// THEN
		verify(logger).trace(ERROR_MESSAGE);
	}

	@Test
	void traceNotEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(false);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger, never()).trace(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void traceEnabledErrorMessageAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(true);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE, EXCEPTION);

		// THEN
		verify(logger).trace(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void traceNotEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(false);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).trace(ERROR_MESSAGE, EXCEPTION);
	}

	@Test
	void traceEnabledErrorMessageWithArguments() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(true);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE_WITH_ARGUMENTS, OBJECT_TYPE, ID);

		// THEN
		verify(logger).trace(ERROR_MESSAGE_WITH_ARGUMENTS, new Object[]{ OBJECT_TYPE, ID });
	}

	@Test
	void traceNotEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(false);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).trace(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void traceEnabledErrorMessageWithArgumentsAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(true);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).trace(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void traceNotEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(false);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger, never()).trace(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}

	@Test
	void traceEnabledErrorMessageWithPositionalArgumentsAndThrowable() {
		// GIVEN
		when(logger.isTraceEnabled()).thenReturn(true);

		// WHEN
		cut.trace(logger, ERROR_MESSAGE_WITH_POSITIONAL_ARGUMENTS, EXCEPTION, OBJECT_TYPE, ID);

		// THEN
		verify(logger).trace(FORMATTED_ERROR_MESSAGE_WITH_ARGUMENTS, EXCEPTION);
	}
}