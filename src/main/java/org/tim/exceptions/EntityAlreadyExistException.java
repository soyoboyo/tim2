package org.tim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistException extends RuntimeException {
	public EntityAlreadyExistException(String entityName) {
		super("Sorry, " + entityName + " for given parameters already exists!");
	}
}
