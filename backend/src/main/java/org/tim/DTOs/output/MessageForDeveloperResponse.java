package org.tim.DTOs.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tim.entities.Translation;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class MessageForDeveloperResponse {

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

	private String createdBy;

	private String projectId;

	private List<Translation> translations;

	private List<String> missingLocales;

	private Map<String, Integer> translationStatuses = new HashMap<>();

	public Boolean isTranslationOutdated(Translation translation) {
		return !translation.getUpdateDate().after(this.getUpdateDate());
	}
}
