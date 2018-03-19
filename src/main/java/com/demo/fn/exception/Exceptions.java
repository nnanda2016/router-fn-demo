package com.demo.fn.exception;

/**
 * Exceptions to be thrown from the system.
 * 
 * @author Niranjan Nanda
 */
public enum Exceptions {
	APP_400001("APP_400001", "Required header '%s' is missing in the request.", 400),
	APP_400002("APP_400002", "Resource ID must be provided in the request path for '%s' operation.", 400);
	
	private final String code;
	private final String message;
	private final int httpStatusCode;
	
	private Exceptions(final String code, final String message, final int httpStatusCode) {
		this.code = code;
		this.message = message;
		this.httpStatusCode = httpStatusCode;
	}
	
	public String exceptionCode() {
		return this.code;
	}
	public String exceptionMessage() {
		return this.message;
	}
	public int httpStatusCode() {
		return this.httpStatusCode;
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(exceptionCode());
		builder.append(": ");
		builder.append(exceptionMessage());
		return builder.toString();
	}
}
