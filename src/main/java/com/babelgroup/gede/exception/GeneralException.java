package com.babelgroup.gede.exception;

import org.springframework.http.HttpStatus;

/**
 * The Class GeneralException.
 */
public class GeneralException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1017453737137116836L;

	/** The status code. */
	private final int statusCode;

	/** The message. */
	private final String message;

	public GeneralException(String message, Throwable cause) {
		super(message, cause);
		this.statusCode = 0;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Instantiates a new general exception.
	 *
	 * @param status  the status
	 * @param message the message
	 */
	public GeneralException(HttpStatus status, String message) {
		super();
		this.statusCode = status.value();
		this.message = message;
	}

	/**
	 * Instantiates a new general exception.
	 *
	 * @param status the status
	 */
	public GeneralException(HttpStatus status) {
		super();
		this.statusCode = status.value();
		this.message = status.getReasonPhrase();
	}

	/**
	 * Instantiates a new general exception.
	 *
	 * @param message the message
	 */
	public GeneralException(String message) {
		super();
		this.statusCode = HttpStatus.BAD_REQUEST.value();
		this.message = message;
	}

	/**
	 * Instantiates a new general exception.
	 */
	public GeneralException() {
		super();
		this.statusCode = HttpStatus.BAD_REQUEST.value();
		this.message = "";
	}
}
