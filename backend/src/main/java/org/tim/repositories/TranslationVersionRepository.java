package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationVersion;

import java.util.List;

@Repository
public interface TranslationVersionRepository extends JpaRepository<TranslationVersion, Long> {
	List<TranslationVersion> findAllByTranslationIdOrderByUpdateDateDesc(Long originalTranslationId);

	List<TranslationVersion> findAllByTranslationId(Long translationId);
}
