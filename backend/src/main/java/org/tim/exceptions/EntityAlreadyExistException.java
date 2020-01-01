package org.tim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ENTITY_ALREADY_EXISTS")
public class EntityAlreadyExistException extends RuntimeException {

	public EntityAlreadyExistException(String entityName) {
		super("Sorry, " + entityName + " for given parameters already exists!");
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
