package org.tim.validators;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.tim.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

public class DTOValidator {

	public static void validate(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorsList = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			throw new ValidationException(errorsList.stream().collect(Collectors.joining(". \n")));
		}
	}
}
