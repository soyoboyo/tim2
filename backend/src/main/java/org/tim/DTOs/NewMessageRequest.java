package org.tim.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.tim.entities.Translation;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class NewMessageRequest {

	@NotBlank(message = "Message key can't be blank")
	private String key;

	@NotBlank(message = "Message content can't be blank")
	private String content;

	private String description;

	@NotBlank(message = "ProjectId is required")
	private String projectId;

}
