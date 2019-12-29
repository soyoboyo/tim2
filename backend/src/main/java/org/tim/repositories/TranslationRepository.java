package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Translation;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface TranslationRepository extends ElasticsearchRepository<Translation, String> {

	List<Translation> findTranslationsByLocaleAndProjectIdAndIsValidTrue(Locale locale, String projectId);

	List<Translation> findTranslationsByLocaleAndProjectId(Locale locale, String projectId);

	Optional<Translation> findTranslationsByLocaleAndMessageId(Locale locale, String messageId);

	Optional<Translation> findTranslationByLocaleAndMessageId(Locale locale, String messageId);

	List<Translation> findTranslationsByMessageId(String messageId);

	List<Translation> findAllByMessageId(String messageId);
}
