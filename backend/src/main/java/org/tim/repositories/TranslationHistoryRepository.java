package org.tim.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationHistory;

import java.util.List;

@Repository
public interface TranslationHistoryRepository extends CrudRepository<TranslationHistory, String> {
    List<TranslationHistory> findAllByTranslationIdOrderByUpdateDateDesc(String originalTranslationId);

    default List<TranslationHistory> findAllByTranslationIdSorted(String originalTranslationId) {
        return findAllByTranslationIdOrderByUpdateDateDesc(originalTranslationId);
    }
}
