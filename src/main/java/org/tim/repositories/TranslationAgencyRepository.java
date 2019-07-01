package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.TranslationAgency;

@Repository
public interface TranslationAgencyRepository extends JpaRepository<TranslationAgency, Long> {
}
