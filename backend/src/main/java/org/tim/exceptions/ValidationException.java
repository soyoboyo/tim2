package org.tim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "VALIDATION_EXCEPTION")
public class ValidationException extends RuntimeException {

	public ValidationException(String msg) {
		super(msg);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
