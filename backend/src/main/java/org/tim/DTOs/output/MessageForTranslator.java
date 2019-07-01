package org.tim.DTOs.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tim.DTOs.output.TranslationForTranslator;

import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageForTranslator {

	@NotNull
	@NonNull
	private Long id;

	@NotBlank
	@NonNull
	private String key;

	@NotBlank
	@NonNull
	private String content;

	private String description;

	@Version
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updateDate;

	private TranslationForTranslator translation;

	private TranslationForTranslator substitute;

	private String previousMessageContent;

}
