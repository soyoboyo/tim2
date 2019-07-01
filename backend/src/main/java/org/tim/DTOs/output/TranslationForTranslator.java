package org.tim.DTOs.output;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TranslationForTranslator {

	@NotNull
	@NonNull
	private Long id;

	@NotNull
	@NonNull
	private String content;

	@NotNull
	@NonNull
	private String locale;

	@NotNull
	@NonNull
	private LocalDateTime updateDate;

	@NotNull
	@NonNull
	private Long messageId;

	private Boolean isValid;

}
