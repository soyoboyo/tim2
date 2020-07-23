package org.tim.DTOs.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LocaleDTO {

	private String name;
	private String fullLanguageName;
	private String fullCountryName;
}
