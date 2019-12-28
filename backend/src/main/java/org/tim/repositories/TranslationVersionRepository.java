package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationVersion;

import java.util.List;

@Repository
public interface TranslationVersionRepository extends ElasticsearchRepository<TranslationVersion, String> {
    List<TranslationVersion> findAllByTranslationIdOrderByUpdateDateDesc(Long originalTranslationId);
}
