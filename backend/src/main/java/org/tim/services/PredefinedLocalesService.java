package org.tim.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PredefinedLocalesService {

    public LinkedHashSet<String> getPredefinedLanguages() {
        Locale locales[] = Locale.getAvailableLocales();
        LinkedHashSet<String> languages = new LinkedHashSet<>();
        for(int i = 0; i < locales.length; i++){
            if(!(locales[i].getLanguage().isEmpty() || locales[i].getCountry().isEmpty())) {
                languages.add(locales[i].getLanguage() + " (" + locales[i].getDisplayLanguage() + ")");
            }
        }
        return languages;
    }

    public LinkedHashSet<String> getPredefinedCounties() {
        Locale locales[] = Locale.getAvailableLocales();
        LinkedHashSet<String> countries = new LinkedHashSet<>();
        for(int i = 0; i < locales.length; i++){
            if(!(locales[i].getLanguage().isEmpty() || locales[i].getCountry().isEmpty() ||
                    !locales[i].getDisplayCountry().matches("[A-Z][a-zA-Z ]{0,}[a-z]") ||
                    !locales[i].getCountry().matches("[a-zA-Z]{1,}")))
                countries.add(locales[i].getCountry() + " (" + locales[i].getDisplayCountry() + ")");
        }
        return countries;
    }
}