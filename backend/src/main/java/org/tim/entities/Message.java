package org.tim.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Message {

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

	@Version
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@UpdateTimestamp
	private LocalDateTime updateDate;

	@ManyToOne(cascade = {
			CascadeType.MERGE})
	@JoinColumn(name = "project_id")
	@NonNull
	@JsonIgnore
	private Project project;

	@NotNull
	private Boolean isArchived = false;

	private String createdBy;

	public Boolean isTranslationOutdated(Translation translation) {
		return !translation.getUpdateDate().isAfter(this.getUpdateDate());
	}

	@PrePersist
	public void prePersist() {
		String username = "Seeder";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			username = authentication.getName();
		}
		this.createdBy = username;
	}
}
