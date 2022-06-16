package nl.craftsmen.exceptionhandling;

import lombok.experimental.StandardException;

@StandardException
// TODO remove the constructors once the new Intellij Lombok plugin recognizes this
// See: https://github.com/mplushnikov/lombok-intellij-plugin/issues/1076
public class BatchjobException extends RuntimeException {

	private static final long serialVersionUID = -4108537027815479772L;

	public BatchjobException(String message, Exception exception) {
		super(message, exception);
	}
}
