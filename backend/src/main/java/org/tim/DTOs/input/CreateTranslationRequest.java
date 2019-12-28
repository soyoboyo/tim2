package org.tim.DTOs.input;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTranslationRequest {

	@NotBlank(message = "Content can't be blank")
	private String content;

	@NotBlank(message = "Locale can't be blank")
	private String locale;
}
