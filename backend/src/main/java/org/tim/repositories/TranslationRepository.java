package org.tim.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Translation;
import org.tim.utils.Pages;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TranslationRepository extends CrudRepository<Translation, String> {

	List<Translation> findTranslationsByLocaleInAndProjectIdAndMessageIdIn(Set<Locale> locale, String projectId, Set<String> messageId, Pageable pageable);

	List<Translation> findTranslationsByLocaleAndProjectIdAndIsValidTrue(Locale locale, String projectId, Pageable pageable);

	Optional<Translation> findTranslationsByLocaleAndMessageId(Locale locale, String messageId);

	Optional<Translation> findTranslationByLocaleAndMessageId(Locale locale, String messageId);

	List<Translation> findTranslationsByMessageId(String messageId, Pageable pageable);

	List<Translation> findAllByMessageId(String messageId, Pageable pageable);

	default List<Translation> findTranslationsToMessages(String projectId, Set<String> messageId, Set<Locale> locale) {
		return findTranslationsByLocaleInAndProjectIdAndMessageIdIn(locale, projectId, messageId, Pages.all());
	}
}
