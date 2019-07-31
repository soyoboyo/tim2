package org.tim;

import org.junit.jupiter.api.Test;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Project;
import org.tim.exceptions.ValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

class ProjectTestsCT {

	@Test
	void whenUpdateSubstituteLocaleAndNoCyclesAndBothLocalesInTargetLocalesThenSuccess() {
		//given
		Project project = random(Project.class);
		LocaleWrapper childLocale = random(LocaleWrapper.class);
		LocaleWrapper parentLocale = random(LocaleWrapper.class);
		project.addTargetLocale(Arrays.asList(childLocale, parentLocale));
		//when
		project.updateSubstituteLocale(childLocale, parentLocale);
		Optional<LocaleWrapper> actualLocale = project.getSubstituteLocale(childLocale);
		//then
		assertAll(
				() -> assertTrue(actualLocale.isPresent()),
				() -> assertEquals(actualLocale.get(), parentLocale)
		);
	}

	@Test
	void whenUpdateSubstituteLocaleAndLocalesIsMissingInTargetLocalesThenFailure() {
		//given
		Project project = random(Project.class);
		LocaleWrapper childLocale = random(LocaleWrapper.class);
		LocaleWrapper parentLocale = random(LocaleWrapper.class);
		project.addTargetLocale(Collections.singletonList(childLocale));
		//when
		Optional<LocaleWrapper> actualLocale = project.getSubstituteLocale(childLocale);
		//then
		assertAll(
				() -> assertThrows(ValidationException.class, () -> project.updateSubstituteLocale(childLocale, parentLocale)),
				() -> assertFalse(actualLocale.isPresent())
		);
	}

	@Test
	void whenUpdateSubstituteLocaleAndGraphHasCyclesThenFailure() {
		//given
		Project project = random(Project.class);
		LocaleWrapper firstLocale = random(LocaleWrapper.class);
		LocaleWrapper secondLocale = random(LocaleWrapper.class);
		LocaleWrapper thirdLocale = random(LocaleWrapper.class);
		LocaleWrapper fourthLocale = random(LocaleWrapper.class);
		project.addTargetLocale(Arrays.asList(firstLocale, secondLocale, thirdLocale, fourthLocale));
		project.updateSubstituteLocale(firstLocale, secondLocale);
		project.updateSubstituteLocale(secondLocale, thirdLocale);
		project.updateSubstituteLocale(thirdLocale, fourthLocale);
		//when
		Optional<LocaleWrapper> actualLocale = project.getSubstituteLocale(fourthLocale);
		//then
		assertAll(
				() -> assertThrows(ValidationException.class, () -> project.updateSubstituteLocale(fourthLocale, firstLocale)),
				() -> assertFalse(actualLocale.isPresent())
		);
	}

	@Test
	void removeTargetLocale_RemovedLocaleIsPresentInReplacementMapAsKey_ReplacementMapEntryIsRemoved() {
		//given
		Project project = random(Project.class);
		LocaleWrapper locale1 = random(LocaleWrapper.class);
		LocaleWrapper locale2 = random(LocaleWrapper.class);
		project.addTargetLocale(Arrays.asList(locale1, locale2));
		project.updateSubstituteLocale(locale1, locale2);
		//when
		project.removeTargetLocale(locale1);
		Optional<LocaleWrapper> actual = project.getSubstituteLocale(locale1);
		//then
		assertFalse(actual.isPresent());
	}


	@Test
	void whenRemoveTargetLocaleAndRemovedLocaleIsPresentInReplacementMapAsValueThenReplacementMapEntriesAreRemoved() {
		//given
		Project project = random(Project.class);
		LocaleWrapper locale1 = random(LocaleWrapper.class);
		LocaleWrapper locale2 = random(LocaleWrapper.class);
		LocaleWrapper locale3 = random(LocaleWrapper.class);
		LocaleWrapper locale4 = random(LocaleWrapper.class);
		project.addTargetLocale(Arrays.asList(locale1, locale2, locale3, locale4));
		project.updateSubstituteLocale(locale2, locale1);
		project.updateSubstituteLocale(locale3, locale1);
		project.updateSubstituteLocale(locale4, locale1);
		//when
		project.removeTargetLocale(locale1);
		Optional<LocaleWrapper> actual2 = project.getSubstituteLocale(locale2);
		Optional<LocaleWrapper> actual3 = project.getSubstituteLocale(locale3);
		Optional<LocaleWrapper> actual4 = project.getSubstituteLocale(locale4);
		//then
		assertAll(
				() -> assertFalse(actual2.isPresent()),
				() -> assertFalse(actual3.isPresent()),
				() -> assertFalse(actual4.isPresent())
		);
	}
}
