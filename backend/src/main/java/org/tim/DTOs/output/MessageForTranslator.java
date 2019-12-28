package org.tim.DTOs.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageForTranslator implements Comparable<MessageForTranslator> {

	@NotNull
	@NonNull
	private String id;

	@NotBlank
	@NonNull
	private String key;

	@NotBlank
	@NonNull
	private String content;

	private String description;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updateDate;

	private TranslationForTranslator translation;

	private TranslationForTranslator substitute;

	private String previousMessageContent;

	@Override
	public int compareTo(MessageForTranslator messageForTranslator) {
		if (this.getTranslation() != null && messageForTranslator.getTranslation() != null) {
			return this.getTranslation().getUpdateDate().compareTo(messageForTranslator.getTranslation().getUpdateDate());
		} else if (this.getTranslation() != null) {
			return this.getTranslation().getUpdateDate().compareTo(messageForTranslator.getUpdateDate());
		} else if (messageForTranslator.getTranslation() != null) {
			return this.getUpdateDate().compareTo(messageForTranslator.getTranslation().getUpdateDate());
		} else {
			return this.getUpdateDate().compareTo(messageForTranslator.getUpdateDate());
		}
	}
}
