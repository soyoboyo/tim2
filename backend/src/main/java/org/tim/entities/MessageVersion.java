
package org.tim.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Document(indexName = "message-version")
@Data
@NoArgsConstructor
public class MessageVersion {

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

	private Date updateDate;

	private String createdBy;

	@NotNull
	private Boolean isArchived;

	@NotNull
	String messageId;
}
