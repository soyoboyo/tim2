package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;
import org.tim.entities.Translation;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface TranslationRepository extends ElasticsearchRepository<Translation, String> {

//	@Query(value = "SELECT T.* FROM TRANSLATION T " +
//			"JOIN MESSAGE M ON M.ID = T.MESSAGE_ID " +
//			"WHERE T.LOCALE ILIKE ?1 AND M.PROJECT_ID = ?2 ;", nativeQuery = true)
//	List<Translation> findTranslationsByLocaleAndProjectId(Locale locale, Long projectId);

    //List<Translation> findTranslationsByLocaleAndProjectIdAndIsValidTrue(Locale locale, String projectId);

    Optional<Translation> findTranslationsByLocaleAndMessageId(Locale locale, String messageId);

	Optional<Translation> findTranslationByLocaleAndMessageId(Locale locale, String messageId);

	List<Translation> findTranslationsByMessageId(String messageId);

	List<Translation> findAllByMessageIdOrderByLocale(String messageId);
}
