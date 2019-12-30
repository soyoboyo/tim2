
package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(indexName = "message-history")
@Data
@NoArgsConstructor
public class MessageHistory {

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
	String messageId;

}
