package org.tim.DTOs.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocaleResponse {

	private String name;
	private String fullLanguageName;
	private String fullCountryName;

}
