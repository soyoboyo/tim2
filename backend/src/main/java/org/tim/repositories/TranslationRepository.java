package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tim.entities.Message;
import org.tim.entities.Translation;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

	@Query(value = "SELECT T.* FROM TRANSLATION T " +
			"JOIN MESSAGE M ON M.ID = T.MESSAGE_ID " +
			"WHERE T.LOCALE ILIKE ?1 AND M.PROJECT_ID = ?2 ;", nativeQuery = true)
	List<Translation> findTranslationsByLocaleAndProjectId(Locale locale, Long projectId);

	List<Translation> findTranslationsByLocaleAndMessage_ProjectIdAndIsValidTrue(Locale locale, Long projectId);

	Optional<Translation> findTranslationsByLocaleAndMessage(Locale locale, Message message);

	Translation findTranslationByLocaleAndMessageId(Locale locale, Long message_id);

	List<Translation> findTranslationsByMessageIdAndIsArchivedFalse(Long messageId);

	List<Translation> findAllByMessageAndIsArchivedFalseOrderByLocale(Message message);

	List<Translation> findAllByMessageId(Long messageId);
}
