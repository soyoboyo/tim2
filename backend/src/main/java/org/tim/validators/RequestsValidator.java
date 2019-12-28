package org.tim.validators;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.tim.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestsValidator {

	public static void validate(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorsList = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			throw new ValidationException(errorsList.stream().collect(Collectors.joining(". \n")));
		}
	}

	public void execute(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorsList = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
			throw new ValidationException(errorsList.stream().collect(Collectors.joining(". \n")));
		}
	}
}

