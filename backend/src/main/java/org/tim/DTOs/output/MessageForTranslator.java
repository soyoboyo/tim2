package org.tim.DTOs.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

	private Date updateDate;

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
