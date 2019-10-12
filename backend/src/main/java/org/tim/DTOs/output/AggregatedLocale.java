package org.tim.DTOs.output;

public class AggregatedLocale {
	private String locale;
	private Integer correct;
	private Integer incorrect;

	private Integer invalid;
	private Integer outdated;
	private Integer missing;

	public AggregatedLocale(String locale, Integer correct, Integer invalid, Integer outdated, Integer missing) {
		this.locale = locale;
		this.correct = correct;
		this.invalid = invalid;
		this.outdated = outdated;
		this.missing = missing;
		this.incorrect = invalid + outdated;
	}
}
