package org.tim.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tim.entities.LocaleWrapper;

import java.util.List;
import java.util.Locale;


@Repository
public interface LocaleWrapperRepository extends JpaRepository<LocaleWrapper, Long> {

	LocaleWrapper findByLocale(Locale locale);

	List<LocaleWrapper> findAllByLocaleIn(List<Locale> locales);

}
