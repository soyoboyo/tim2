package org.tim.DTOs.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationCreateDTO {

	@NotNull
	@NonNull
	@NotBlank(message = "Content can't be blank")
	private String content;

	@NotNull
	@NonNull
	private String locale;
}
