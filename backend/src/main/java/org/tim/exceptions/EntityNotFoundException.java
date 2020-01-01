package org.tim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ENTITY_NOT_FOUND")
public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(String entityName) {
		super("Sorry, we can't find this " + entityName);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
