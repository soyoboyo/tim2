package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

	@Setter(AccessLevel.NONE)
	private Date updateDate;

	@NotNull
	@NonNull
	private Message message;

	private Boolean isValid = true;

	private Boolean isArchived = false;

	private String createdBy;

	public void prePersist() {
		String username = "Seeder";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			username = authentication.getName();
		}
		this.createdBy = username;
	}
}
