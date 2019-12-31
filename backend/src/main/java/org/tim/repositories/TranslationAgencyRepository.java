package org.tim.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationAgency;

@Repository
public interface TranslationAgencyRepository extends CrudRepository<TranslationAgency, String> {
}
