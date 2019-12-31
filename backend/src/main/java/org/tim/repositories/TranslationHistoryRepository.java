package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationHistory;

import java.util.List;

@Repository
public interface TranslationHistoryRepository extends ElasticsearchRepository<TranslationHistory, String> {
    List<TranslationHistory> findAllByTranslationIdOrderByUpdateDateDesc(String originalTranslationId);

    default List<TranslationHistory> findAllByTranslationIdSorted(String originalTranslationId) {
        return findAllByTranslationIdOrderByUpdateDateDesc(originalTranslationId);
    }
}
