package nl.craftsmen.util;

import java.time.format.DateTimeParseException;
import lombok.experimental.StandardException;

@StandardException
// TODO remove the constructors once the new Intellij Lombok plugin recognizes this
// See: https://github.com/mplushnikov/lombok-intellij-plugin/issues/1076
public class DateUtilException extends RuntimeException {

	private static final long serialVersionUID = -8712250427204587433L;

	public DateUtilException(String message) {
		super(message);
	}
	
	public DateUtilException(String message, DateTimeParseException e) {
		super(message, e);
	}
}
