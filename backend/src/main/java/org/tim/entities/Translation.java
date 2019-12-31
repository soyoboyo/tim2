package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Locale;

@Document(indexName = "translation")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Translation {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;

	@NotNull
	private String content;

	@NotNull
	@NonNull
	private Locale locale;

	private Date updateDate = new Date();

	@NotNull
	@NonNull
	private String messageId;

	@NonNull
	@NotNull
	private String projectId;

	private boolean isValid = true;

	private String createdBy;

}
