package nl.craftsmen.exceptionhandling;

import lombok.experimental.StandardException;

@StandardException
// TODO remove the constructors once the new Intellij Lombok plugin recognizes this
// See: https://github.com/mplushnikov/lombok-intellij-plugin/issues/1076
public class UnexpectedTestException extends RuntimeException {

	private static final long serialVersionUID = -6791687065206716292L;

	private static final String MESSAGE = "Didn't expect an exception to be thrown!";

	public UnexpectedTestException() {
		super(MESSAGE);
	}

	public UnexpectedTestException(Exception exception) {
		super(MESSAGE, exception);
	}
}
