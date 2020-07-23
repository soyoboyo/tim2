package org.tim.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDTO {

	@NotBlank(message = "Message can't be blank")
	private String content;

	@NotBlank(message = "You must define the language of translation")
	private String locale;

	@NotNull(message = "You must define what message you are translating")
	private Long messageId;

	private Boolean isValid;

}
