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

    List<Translation> findTranslationsByLocaleAndMessage_ProjectIdAndIsValidTrue(Locale locale, String projectId);

    Optional<Translation> findTranslationsByLocaleAndMessage(Locale locale, Message message);

	Translation findTranslationByLocaleAndMessageId(Locale locale, String message_id);

	List<Translation> findTranslationsByMessageIdAndIsArchivedFalse(String messageId);

	List<Translation> findAllByMessageAndIsArchivedFalseOrderByLocale(Message message);
}
