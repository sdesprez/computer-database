package com.excilys.computerdatabase.exceptions;

/**
 * RuntimeException for the exceptions of the Persistence layer
 */
public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PersistenceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(final String message) {
		super(message);
	}

	public PersistenceException(final Throwable cause) {
		super(cause);
	}

}
