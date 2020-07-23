package org.tim.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TranslationAgency {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@NonNull
	private String name;

	@Email
	private String email;

	@NonNull
	@ManyToMany(cascade = {
			CascadeType.MERGE, CascadeType.DETACH},
			fetch = FetchType.EAGER)
	@JoinTable(name = "translation_agency_locale_wrapper",
			joinColumns = @JoinColumn(name = "translation_agency_id"),
			inverseJoinColumns = @JoinColumn(name = "locale_wrapper_id"))
	private List<LocaleWrapper> acceptedLocales;
}
