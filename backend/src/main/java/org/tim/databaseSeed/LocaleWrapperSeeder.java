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

		LocaleWrapper pl_PL = new LocaleWrapper(new Locale("pl", "PL"));
		locales.put("pl_PL", localeWrapperRepository.save(pl_PL));

		LocaleWrapper de_DE = new LocaleWrapper(Locale.GERMANY);
		locales.put("de_DE", localeWrapperRepository.save(de_DE));

		LocaleWrapper ar_LY = new LocaleWrapper(new Locale("ar", "LY"));
		locales.put("ar_LY", localeWrapperRepository.save(ar_LY));

		LocaleWrapper ko_KR = new LocaleWrapper(new Locale("ko", "KR"));
		locales.put("ko_KR", localeWrapperRepository.save(ko_KR));

		LocaleWrapper pl_DE = new LocaleWrapper(new Locale("pl", "DE"));
		locales.put("pl_DE", localeWrapperRepository.save(pl_DE));

		locales.put("af_NA", localeWrapperRepository.save(new LocaleWrapper(new Locale("af", "NA"))));
		locales.put("af_ZA", localeWrapperRepository.save(new LocaleWrapper(new Locale("af", "ZA"))));
		locales.put("ak_GH", localeWrapperRepository.save(new LocaleWrapper(new Locale("ak", "GH"))));
		locales.put("sq_AL", localeWrapperRepository.save(new LocaleWrapper(new Locale("sq", "AL"))));
		locales.put("hy_AM", localeWrapperRepository.save(new LocaleWrapper(new Locale("hy", "AM"))));
		locales.put("ar_DZ", localeWrapperRepository.save(new LocaleWrapper(new Locale("ar", "DZ"))));
		locales.put("ar_SD", localeWrapperRepository.save(new LocaleWrapper(new Locale("ar", "SD"))));
		locales.put("ar_EG", localeWrapperRepository.save(new LocaleWrapper(new Locale("ar", "EG"))));
		locales.put("bn_IN", localeWrapperRepository.save(new LocaleWrapper(new Locale("bn", "IN"))));
		locales.put("bs_BA", localeWrapperRepository.save(new LocaleWrapper(new Locale("bs", "BA"))));
		locales.put("bg_BG", localeWrapperRepository.save(new LocaleWrapper(new Locale("bg", "BG"))));
		locales.put("my_MM", localeWrapperRepository.save(new LocaleWrapper(new Locale("my", "MM"))));
		locales.put("ca_ES", localeWrapperRepository.save(new LocaleWrapper(new Locale("ca", "ES"))));
		locales.put("kw_GB", localeWrapperRepository.save(new LocaleWrapper(new Locale("kw", "GB"))));
		locales.put("da_DK", localeWrapperRepository.save(new LocaleWrapper(new Locale("da", "DK"))));
		locales.put("en_AS", localeWrapperRepository.save(new LocaleWrapper(new Locale("en", "AS"))));
		locales.put("en_AU", localeWrapperRepository.save(new LocaleWrapper(new Locale("en", "AU"))));
		locales.put("en_VI", localeWrapperRepository.save(new LocaleWrapper(new Locale("en", "VI"))));
		locales.put("en_ZA", localeWrapperRepository.save(new LocaleWrapper(new Locale("en", "ZA"))));
		locales.put("fr_MG", localeWrapperRepository.save(new LocaleWrapper(new Locale("fr", "MG"))));
		locales.put("fr_MC", localeWrapperRepository.save(new LocaleWrapper(new Locale("fr", "MC"))));
		locales.put("fr_BL", localeWrapperRepository.save(new LocaleWrapper(new Locale("fr", "BL"))));
		locales.put("gu_IN", localeWrapperRepository.save(new LocaleWrapper(new Locale("gu", "IN"))));

		return locales;
	}
}
