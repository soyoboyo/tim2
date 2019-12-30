package org.tim.DTOs;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateMessageRequest {

	@NotBlank(message = "ProjectId is required")
	private String projectId;

	@NotBlank(message = "Message key can't be blank")
	private String key;

	@NotBlank(message = "Message content can't be blank")
	private String content;

	private String description;

}
