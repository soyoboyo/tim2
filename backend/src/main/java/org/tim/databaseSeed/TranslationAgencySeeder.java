package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.TranslationAgency;
import org.tim.repositories.TranslationAgencyRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationAgencySeeder {

	private final TranslationAgencyRepository translationAgencyRepository;

	public Map<String, TranslationAgency> initTransactionAgencies(Map<String, LocaleWrapper> locales) {

		Map<String, TranslationAgency> translationAgencies = new HashMap<>();

		TranslationAgency germanicaTA = new TranslationAgency("Germanica", Arrays.asList(locales.get("de_DE")));
		germanicaTA.setEmail("germanica@mail.com");
		translationAgencies.put("germanica", translationAgencyRepository.save(germanicaTA));

		TranslationAgency linguaNovaTA = new TranslationAgency("Lingua Nova", Arrays.asList(locales.get("en_US"), locales.get("en_UK")));
		linguaNovaTA.setEmail("lingua.nova@mail.com");
		translationAgencies.put("linguaNova", translationAgencyRepository.save(linguaNovaTA));

		TranslationAgency turboTłumaczeniaTA = new TranslationAgency("TurboTłumaczenia",
				Arrays.asList(locales.get("en_US"), locales.get("en_GB"), locales.get("pl_PL"), locales.get("de_DE"), locales.get("ko_KR"), locales.get("ar_LY")));
		turboTłumaczeniaTA.setEmail("turbotlumaczenia@mail.com");
		translationAgencies.put("turboTłumaczenia", translationAgencyRepository.save(turboTłumaczeniaTA));

		return translationAgencies;
	}
}
