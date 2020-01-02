package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(indexName = "message")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;

	@NotBlank
	@NonNull
	private String key;

	@NotBlank
	@NonNull
	private String content;

	private String description;

	private Date updateDate = new Date();

	@NonNull
	private String projectId;

	@NotNull
	private boolean isArchived = false;
	// Format required by ElasticSearch
	public boolean getIsArchived() {
		return isArchived;
	}

	private String createdBy;

	public boolean isTranslationOutdated(Translation translation) {
		return !translation.getUpdateDate().after(this.getUpdateDate());
	}
}
