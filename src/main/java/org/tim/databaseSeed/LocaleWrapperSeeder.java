package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.repositories.LocaleWrapperRepository;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocaleWrapperSeeder {

    private final LocaleWrapperRepository localeWrapperRepository;

    public Map<String, LocaleWrapper> initLocales() {
        Map<String, LocaleWrapper> locales = new HashMap<>();

        LocaleWrapper en_US = new LocaleWrapper(Locale.US);
        locales.put("en_US", localeWrapperRepository.save(en_US));

        LocaleWrapper en_GB = new LocaleWrapper(Locale.UK);
        locales.put("en_GB", localeWrapperRepository.save(en_GB));

        LocaleWrapper pl_PL  = new LocaleWrapper(new Locale("pl", "PL"));
        locales.put("pl_PL", localeWrapperRepository.save(pl_PL));

        LocaleWrapper de_DE = new LocaleWrapper(Locale.GERMANY);
        locales.put("de_DE", localeWrapperRepository.save(de_DE));

        LocaleWrapper ar_LY = new LocaleWrapper(new Locale("ar", "LY"));
        locales.put("ar_LY", localeWrapperRepository.save(ar_LY));

        LocaleWrapper ko_KR = new LocaleWrapper(new Locale("ko", "KR"));
        locales.put("ko_KR", localeWrapperRepository.save(ko_KR));

        LocaleWrapper pl_DE = new LocaleWrapper(new Locale("pl", "DE"));
        locales.put("pl_DE", localeWrapperRepository.save(pl_DE));

        return locales;
    }
}
