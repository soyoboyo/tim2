package org.tim.DTOs.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MessageForDeveloper {

	@NonNull
	private Long id;

	@NotBlank
	@NonNull
	private String key;

	@NotBlank
	@NonNull
	private String content;

	private String description;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updateDate;

	private String createdBy;

	private Long projectId;

	private List<TranslationForDeveloper> translations;

	private List<String> missingLocales;

}
