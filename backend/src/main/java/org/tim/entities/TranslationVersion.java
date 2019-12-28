package org.tim.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Locale;

@Document(indexName = "translation-version")
@Data
public class TranslationVersion {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;

	public TranslationVersion() {
		updateDate = new Date();
	}

	@NotNull
	private String content;

	@NotNull
	private Locale locale;

	private Date updateDate;

	private Boolean isValid;

	private String createdBy;

	private Boolean isArchived;

	@NotNull
	private String translationId;
}
