package org.tim.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.actuate.audit.listener.AuditListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@EntityListeners(AuditListener.class)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Translation {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String content;

	@NotNull
	@NonNull
	private Locale locale;

	@Setter(AccessLevel.NONE)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@UpdateTimestamp
	private LocalDateTime updateDate;

	@NotNull
	@NonNull
	@ManyToOne(cascade = {
			CascadeType.MERGE})
	@JoinColumn(name = "message_id")
	private Message message;

	private Boolean isValid = true;

	private Boolean isArchived = false;

	private String createdBy;

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
