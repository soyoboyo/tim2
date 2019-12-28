package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.entities.TranslationAgency;
import org.tim.repositories.TranslationAgencyRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationAgencySeeder {

    private final TranslationAgencyRepository translationAgencyRepository;

    public Map<String, TranslationAgency> initTransactionAgencies() {

        Map<String, TranslationAgency> translationAgencies = new HashMap<>();

        TranslationAgency germanicaTA = new TranslationAgency("Germanica", Arrays.asList(createLocale("de_DE")));
        germanicaTA.setEmail("germanica@mail.com");
        translationAgencies.put("germanica", translationAgencyRepository.save(germanicaTA));

        TranslationAgency linguaNovaTA = new TranslationAgency("Lingua Nova", Arrays.asList(createLocale("en_US"), createLocale("en_UK")));
        linguaNovaTA.setEmail("lingua.nova@mail.com");
        translationAgencies.put("linguaNova", translationAgencyRepository.save(linguaNovaTA));

        TranslationAgency turboTłumaczeniaTA = new TranslationAgency("TurboTłumaczenia",
                Arrays.asList(createLocale("en_US"), createLocale("en_GB"), createLocale("pl_PL"), createLocale("de_DE"), createLocale("ko_KR"), createLocale("ar_LY")));
        turboTłumaczeniaTA.setEmail("turbotlumaczenia@mail.com");
        translationAgencies.put("turboTłumaczenia", translationAgencyRepository.save(turboTłumaczeniaTA));

        return translationAgencies;
    }

    private Locale createLocale(String name) {
        return LocaleUtils.toLocale(name);
    }
}
