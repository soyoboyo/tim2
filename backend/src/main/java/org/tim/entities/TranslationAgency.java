package org.tim.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Locale;

@Document(indexName = "translation-agency")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TranslationAgency {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;

	@NotBlank
	@NonNull
	private String name;

	@Email
	private String email;

	@NonNull
	private List<Locale> acceptedLocales;
}
