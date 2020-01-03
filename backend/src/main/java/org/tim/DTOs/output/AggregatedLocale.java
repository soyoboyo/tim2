package org.tim.DTOs.output;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AggregatedLocale {

	private String locale;
	private Integer correct;
	private Integer incorrect;
	private Integer missing;

	public AggregatedLocale(String locale, Integer correct, Integer incorrect, Integer missing) {
		this.locale = locale;
		this.correct = correct;
		this.incorrect = incorrect;
		this.missing = missing;
	}

}
