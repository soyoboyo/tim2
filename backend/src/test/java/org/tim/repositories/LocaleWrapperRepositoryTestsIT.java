package org.tim.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tim.configuration.SpringJpaTestsCustomExtension;

import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class LocaleWrapperRepositoryTestsIT extends SpringJpaTestsCustomExtension {

//    @Test
//    public void testLocaleWrapperRepositoryFind() {
//        //given
//        LocaleWrapper locale = localeWrapperRepository.save(random(LocaleWrapper.class));
//        //when
//        Optional<LocaleWrapper> actualLocale = localeWrapperRepository.findById(locale.getId());
//        //then
//        assertTrue(actualLocale.isPresent());
//        assertEquals(locale, actualLocale.get());
//    }
//
//    @Test
//    public void testLocaleWrapperRepositoryDelete() {
//        //given
//        LocaleWrapper locale = localeWrapperRepository.save(random(LocaleWrapper.class));
//        localeWrapperRepository.delete(locale);
//        //when
//        Optional<LocaleWrapper> actualLocale = localeWrapperRepository.findById(locale.getId());
//        //then
//        assertFalse(actualLocale.isPresent());
//    }
}