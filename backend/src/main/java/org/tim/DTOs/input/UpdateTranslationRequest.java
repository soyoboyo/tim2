package org.tim.DTOs.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTranslationRequest {

	@NotBlank(message = "Content can't be blank")
	private String content;
}
