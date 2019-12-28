package org.tim.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationAgency;

@Repository
public interface TranslationAgencyRepository extends ElasticsearchRepository<TranslationAgency, String> {
}
