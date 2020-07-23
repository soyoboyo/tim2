package org.tim.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class MessageVersion {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@NotNull
	private Boolean isArchived;

	@NotNull
	Long messageId;
}
