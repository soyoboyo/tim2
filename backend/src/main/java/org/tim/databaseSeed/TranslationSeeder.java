package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;

import org.tim.entities.Message;
import org.tim.entities.Translation;
import org.tim.repositories.TranslationRepository;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationSeeder {

    private final TranslationRepository translationRepository;
    public void initTranslations(Map<String, Message> messages) {

        Translation translationT1M1 = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("messageM1"));
        translationT1M1.setContent("Artykuły spożywcze, które pokochasz, idealnie dostarczone.");

        Translation translationT2M1 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM1"));
        translationT2M1.setContent("Boodschappen waar u van houdt, perfect afgeleverd.");

        Translation translationT3M1 = new Translation(LocaleUtils.toLocale("en_US"), messages.get("messageM1"));
        translationT3M1.setContent("Groceries you’ll love, perfectly delivered.");

        Translation translationT4M1 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM1"));
        translationT4M1.setContent("당신이 좋아할 음식, 완벽하게 배달되는 식료품.");

        Translation translationT1M2 = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("messageM2"));
        translationT1M2.setContent("Niska cena gwarantowana.");

        Translation translationT2M2 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM2"));
        translationT2M2.setContent("Lage prijsbelofte.");

        Translation translationT3M2 = new Translation(LocaleUtils.toLocale("en_US"), messages.get("messageM2"));
        translationT3M2.setContent("Low Price Promise.");

        Translation translationT4M2 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM2"));
        translationT4M2.setContent("저렴한 가격 약속.");

        Translation translationT1M3 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM3"));
        translationT1M3.setContent("Wir haben ein einzigartiges Geschäftsmodell entwickelt und betreiben, das uns stark positioniert, " +
                "da sich immer mehr Verbraucher für Online-Shopping entscheiden.");

        Translation translationT2M3 = new Translation(LocaleUtils.toLocale("en_GB"), messages.get("messageM3"));
        translationT2M3.setContent("We have developed and operate a unique business model which positions us strongly" +
                " as more consumers choose to shop online.");

        Translation translationT3M3 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM3"));
        translationT3M3.setContent("우리는 더 많은 소비자가 온라인 쇼핑을 선택함에 따라 우리를 강하게 위치시키는 독특한 비즈니스 모델을 개발하고 운영합니다.");

        Translation translationT4M3 = new Translation(LocaleUtils.toLocale("ar_LY"), messages.get("messageM3"));
        translationT4M3.setContent("لقد قمنا بتطوير وتشغيل نموذج أعمال فريد من نوعه يضعنا بقوة مع اختيار المزيد من المستهلكين للتسوق عبر الإنترنت");

        Translation translationT1M4 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM4"));
        translationT1M4.setContent("Unser strategisches Ziel ist es, die Interessen unserer Kunden, Investoren und anderer Stakeholder in " +
                "Einklang zu bringen, um einen langfristigen Shareholder Value zu erzielen.");

        Translation translationT2M4 = new Translation(LocaleUtils.toLocale("en_GB"), messages.get("messageM4"));
        translationT2M4.setContent("Our strategic objective is to align the interests of our customers, " +
                "investors and other stakeholders to deliver long term shareholder value.");

        Translation translationT3M4 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM4"));
        translationT3M4.setContent("우리의 전략적 목표는 고객, 투자자 및 기타 이해 관계자의 이익을 조정하여 장기적인 주주 가치를 제공하는 것입니다.");

        Translation translationT4M4 = new Translation(LocaleUtils.toLocale("ar_LY"), messages.get("messageM4"));
        translationT4M4.setContent("هدفنا الاستراتيجي هو مواءمة مصالح عملائنا والمستثمرين وأصحاب المصلحة الآخرين لتحقيق قيمة طويلة الأجل للمساهمي");


        //all translation needed for "simple web app" presentation
        Translation pricingKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("pricing"));
        pricingKo.setContent("가격");Translation signUpKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("signUp"));
        signUpKo.setContent("가입");Translation ocadoTechnologyKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("ocadoTechnology"));
        ocadoTechnologyKo.setContent("Ocado Technology");Translation featuresNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresNav"));
        featuresNavKo.setContent("풍모");Translation enterpriseNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseNav"));
        enterpriseNavKo.setContent("기업");Translation supportNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("supportNav"));
        supportNavKo.setContent("지원하다");Translation pricingNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("pricingNav"));
        pricingNavKo.setContent("가격");Translation freeCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeCard"));
        freeCardKo.setContent("비어 있는");Translation maxFreeUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxFreeUsers"));
        maxFreeUsersKo.setContent("10 명의 사용자 포함");Translation maxFreeMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxFreeMemoryLimit"));
        maxFreeMemoryLimitKo.setContent("2GB의 저장 용량");
        //Translation freeEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeEmailSupport"));
        //freeEmailSupportKo.setContent("이메일 지원");
        Translation freeHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeHelpCenter"));
        freeHelpCenterKo.setContent("도움말 센터 액세스");Translation signUpToAccessKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("signUpToAccess"));
        signUpToAccessKo.setContent("무료 가입");Translation proCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proCard"));
        proCardKo.setContent("찬성");Translation maxProUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxProUsers"));
        maxProUsersKo.setContent("사용자 20 명 포함");Translation maxProMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxProMemoryLimit"));
        maxProMemoryLimitKo.setContent("10GB의 저장 용량");Translation proEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proEmailSupport"));
        proEmailSupportKo.setContent("이메일 우선 지원");Translation proHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proHelpCenter"));
        proHelpCenterKo.setContent("도움말 센터 액세스");Translation getStartedKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("getStarted"));
        getStartedKo.setContent("시작하다");Translation enterpriseCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseCard"));
        enterpriseCardKo.setContent("기업");
        //Translation maxEnterpriseUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseUsers"));
        //maxEnterpriseUsersKo.setContent("사용자 30 명 포함");Translation maxEnterpriseMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseMemoryLimit"));
        //maxEnterpriseMemoryLimitKo.setContent("15GB의 저장 용량");Translation enterpriseEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseEmailSupport"));
        //enterpriseEmailSupportKo.setContent("전화 및 이메일 지원");Translation enterpriseHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseHelpCenter"));
        //enterpriseHelpCenterKo.setContent("도움말 센터 액세스");
        Translation contactUsKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("contactUs"));
        contactUsKo.setContent("문의하기");Translation featuresFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter"));
        featuresFooterKo.setContent("풍모");Translation featuresFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter1"));
        featuresFooter1Ko.setContent("좋은 것");Translation featuresFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter2"));
        featuresFooter2Ko.setContent("무작위 기능");Translation featuresFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter3"));
        featuresFooter3Ko.setContent("팀 기능");Translation featuresFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter4"));
        featuresFooter4Ko.setContent("개발자 용 자료");Translation featuresFooter5Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter5"));
        featuresFooter5Ko.setContent("다른 것");Translation featuresFooter6Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter6"));
        featuresFooter6Ko.setContent("마지막으로");Translation resourcesFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter"));
        resourcesFooterKo.setContent("자원");Translation resourcesFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter1"));
        resourcesFooter1Ko.setContent("의지");Translation resourcesFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter2"));
        resourcesFooter2Ko.setContent("리소스 이름");Translation resourcesFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter3"));
        resourcesFooter3Ko.setContent("다른 리소스");Translation resourcesFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter4"));
        resourcesFooter4Ko.setContent("최종 자원");Translation aboutFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter"));
        aboutFooterKo.setContent("약");Translation aboutFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter1"));
        aboutFooter1Ko.setContent("팀");Translation aboutFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter2"));
        aboutFooter2Ko.setContent("위치");Translation aboutFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter3"));
        aboutFooter3Ko.setContent("은둔");Translation aboutFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter4"));
        aboutFooter4Ko.setContent("자귀");

        Translation pricingDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("pricing"));
        pricingDe.setContent("Preisgestaltung");Translation signUpDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("signUp"));
        signUpDe.setContent("Anmelden");Translation ocadoTechnologyDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("ocadoTechnology"));
        ocadoTechnologyDe.setContent("Ocado Technology");Translation featuresNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresNav"));
        featuresNavDe.setContent("Eigenschaften");Translation enterpriseNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseNav"));
        enterpriseNavDe.setContent("Unternehmen");Translation supportNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("supportNav"));
        supportNavDe.setContent("Unterstützung");Translation pricingNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("pricingNav"));
        pricingNavDe.setContent("Preisgestaltung");Translation freeCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeCard"));
        freeCardDe.setContent("Kostenlos");Translation maxFreeUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxFreeUsers"));
        maxFreeUsersDe.setContent("10 Benutzer eingeschlossen");Translation maxFreeMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxFreeMemoryLimit"));
        maxFreeMemoryLimitDe.setContent("2 GB Speicher");Translation freeEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeEmailSupport"));
        freeEmailSupportDe.setContent("Email Unterstützung");Translation freeHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeHelpCenter"));
        freeHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");
        //Translation signUpToAccessDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("signUpToAccess"));
        //signUpToAccessDe.setContent("Melde dich kostenlos an");
        Translation proCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proCard"));
        proCardDe.setContent("Profi");Translation maxProUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxProUsers"));
        maxProUsersDe.setContent("20 Benutzer eingeschlossen");Translation maxProMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxProMemoryLimit"));
        maxProMemoryLimitDe.setContent("10 GB Speicher");Translation proEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proEmailSupport"));
        proEmailSupportDe.setContent("Bevorzugte E-Mail-Unterstützung");Translation proHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proHelpCenter"));
        proHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");Translation getStartedDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("getStarted"));
        getStartedDe.setContent("Loslegen");Translation enterpriseCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseCard"));
        enterpriseCardDe.setContent("Unternehmen");Translation maxEnterpriseUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxEnterpriseUsers"));
        maxEnterpriseUsersDe.setContent("30 Benutzer eingeschlossen");Translation maxEnterpriseMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxEnterpriseMemoryLimit"));
        maxEnterpriseMemoryLimitDe.setContent("15 GB Speicher");Translation enterpriseEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseEmailSupport"));
        enterpriseEmailSupportDe.setContent("Telefon- und E-Mail-Support");Translation enterpriseHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseHelpCenter"));
        enterpriseHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");Translation contactUsDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("contactUs"));
        contactUsDe.setContent("Kontaktiere uns");
        //Translation featuresFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter"));
        //featuresFooterDe.setContent("Eigenschaften");
        Translation featuresFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter1"));
        featuresFooter1De.setContent("Cooles Zeug");Translation featuresFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter2"));
        featuresFooter2De.setContent("Zufälliges Merkmal");Translation featuresFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter3"));
        featuresFooter3De.setContent("Team-Funktion");Translation featuresFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter4"));
        featuresFooter4De.setContent("Zeug für Entwickler");Translation featuresFooter5De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter5"));
        featuresFooter5De.setContent("Noch einer");Translation featuresFooter6De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter6"));
        featuresFooter6De.setContent("Ostatni raz");Translation resourcesFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter"));
        resourcesFooterDe.setContent("Ressourcen");Translation resourcesFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter1"));
        resourcesFooter1De.setContent("Ressource");Translation resourcesFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter2"));
        resourcesFooter2De.setContent("Ressourcenname");Translation resourcesFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter3"));
        resourcesFooter3De.setContent("Eine weitere Ressource");Translation resourcesFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter4"));
        resourcesFooter4De.setContent("Letzte Ressource");Translation aboutFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter"));
        aboutFooterDe.setContent("Über");Translation aboutFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter1"));
        aboutFooter1De.setContent("Mannschaft");Translation aboutFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter2"));
        aboutFooter2De.setContent("Standorte");Translation aboutFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter3"));
        aboutFooter3De.setContent("Privatsphäre");Translation aboutFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter4"));
        aboutFooter4De.setContent("Nutzungsbedingungen");

        Translation pricingPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("pricing"));
        pricingPl.setContent("Cennik");Translation signUpPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("signUp"));
        signUpPl.setContent("Zaloguj się");Translation ocadoTechnologyPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("ocadoTechnology"));
        ocadoTechnologyPl.setContent("Ocado Technology");Translation featuresNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresNav"));
        featuresNavPl.setContent("Funkcje");Translation enterpriseNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseNav"));
        enterpriseNavPl.setContent("Przedsiębiorstwo");Translation supportNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("supportNav"));
        supportNavPl.setContent("Wsparcie");Translation pricingNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("pricingNav"));
        pricingNavPl.setContent("Cennik");Translation freeCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeCard"));
        freeCardPl.setContent("Darmowy");Translation maxFreeUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxFreeUsers"));
        maxFreeUsersPl.setContent("Dołącz 10 użytkowników");
        //Translation maxFreeMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxFreeMemoryLimit"));
        //maxFreeMemoryLimitPl.setContent("2 GB pamięci");
        Translation freeEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeEmailSupport"));
        freeEmailSupportPl.setContent("Wsparcie emailowe");Translation freeHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeHelpCenter"));
        freeHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation signUpToAccessPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("signUpToAccess"));
        signUpToAccessPl.setContent("Zarejestruj się za darmo");Translation proCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proCard"));
        proCardPl.setContent("Pro");Translation maxProUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxProUsers"));
        maxProUsersPl.setContent("Dołącz 20 użytkowników");Translation maxProMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxProMemoryLimit"));
        maxProMemoryLimitPl.setContent("10 GB pamięci");Translation proEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proEmailSupport"));
        proEmailSupportPl.setContent("Priorytetowa obsługa wsparcia e-mail");Translation proHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proHelpCenter"));
        proHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation getStartedPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("getStarted"));
        getStartedPl.setContent("Rozpocznij");
        //Translation enterpriseCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseCard"));
        //enterpriseCardPl.setContent("Przedsiębiorstwo");
        Translation maxEnterpriseUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxEnterpriseUsers"));
        maxEnterpriseUsersPl.setContent("Dołącz 30 użytkowników");Translation maxEnterpriseMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxEnterpriseMemoryLimit"));
        maxEnterpriseMemoryLimitPl.setContent("15 GB pamięci");Translation enterpriseEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseEmailSupport"));
        enterpriseEmailSupportPl.setContent("Wsparcie telefoniczne i e-mailowe");Translation enterpriseHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseHelpCenter"));
        enterpriseHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation contactUsPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("contactUs"));
        contactUsPl.setContent("Skontaktuj się z nami");Translation featuresFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter"));featuresFooterPl.setContent("Funkcje");Translation featuresFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter1"));
        featuresFooter1Pl.setContent("Fajne rzeczy");Translation featuresFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter2"));
        featuresFooter2Pl.setContent("Losowa funkcja");Translation featuresFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter3"));
        featuresFooter3Pl.setContent("Funkcja zespołu");Translation featuresFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter4"));
        featuresFooter4Pl.setContent("Rzeczy dla programistów");Translation featuresFooter5Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter5"));
        featuresFooter5Pl.setContent("Inny");
        //Translation featuresFooter6Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter6"));
        //featuresFooter6Pl.setContent("자원");
        Translation resourcesFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter"));
        resourcesFooterPl.setContent("Zasoby");Translation resourcesFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter1"));
        resourcesFooter1Pl.setContent("Zasób");Translation resourcesFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter2"));
        resourcesFooter2Pl.setContent("Nazwa zasobu");Translation resourcesFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter3"));
        resourcesFooter3Pl.setContent("Inny zasób");Translation resourcesFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter4"));
        resourcesFooter4Pl.setContent("Ostateczny zasób");Translation aboutFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter"));
        aboutFooterPl.setContent("Infornacje");Translation aboutFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter1"));
        aboutFooter1Pl.setContent("Zespół");Translation aboutFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter2"));
        aboutFooter2Pl.setContent("Lokalizacje");Translation aboutFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter3"));
        aboutFooter3Pl.setContent("Prywatność");Translation aboutFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter4"));
        aboutFooter4Pl.setContent("Warunki");


        translationRepository.saveAll(Arrays.asList(translationT1M1, translationT1M2, translationT1M3, translationT2M1,
                translationT2M2, translationT2M3, translationT2M4, translationT3M1, translationT3M2, translationT1M4,
                translationT4M1, translationT4M2, translationT3M3, translationT3M4, translationT4M3, translationT4M4,
                pricingKo, signUpKo, ocadoTechnologyKo, featuresNavKo, enterpriseNavKo, supportNavKo, pricingNavKo,
                freeCardKo, maxFreeUsersKo, maxFreeMemoryLimitKo, freeHelpCenterKo, signUpToAccessKo,
                proCardKo, maxProUsersKo, maxProMemoryLimitKo, proEmailSupportKo, proHelpCenterKo, getStartedKo,
                enterpriseCardKo,  contactUsKo, featuresFooterKo, featuresFooter1Ko, featuresFooter2Ko,
                featuresFooter3Ko, featuresFooter4Ko, featuresFooter5Ko, featuresFooter6Ko, resourcesFooterKo,
                resourcesFooter1Ko, resourcesFooter2Ko, resourcesFooter3Ko, resourcesFooter4Ko, aboutFooterKo,
                aboutFooter1Ko, aboutFooter2Ko, aboutFooter3Ko, aboutFooter4Ko, pricingDe, signUpDe,
                ocadoTechnologyDe, featuresNavDe, enterpriseNavDe, supportNavDe, pricingNavDe, freeCardDe,
                maxFreeUsersDe, maxFreeMemoryLimitDe, freeEmailSupportDe, freeHelpCenterDe,
                proCardDe, maxProUsersDe, maxProMemoryLimitDe, proEmailSupportDe, proHelpCenterDe, getStartedDe,
                enterpriseCardDe, maxEnterpriseUsersDe, maxEnterpriseMemoryLimitDe, enterpriseEmailSupportDe,
                enterpriseHelpCenterDe, contactUsDe, featuresFooter1De, featuresFooter2De,
                featuresFooter3De, featuresFooter4De, featuresFooter5De, featuresFooter6De, resourcesFooterDe,
                resourcesFooter1De, resourcesFooter2De, resourcesFooter3De, resourcesFooter4De, aboutFooterDe,
                aboutFooter1De, aboutFooter2De, aboutFooter3De, aboutFooter4De, pricingPl, signUpPl, ocadoTechnologyPl,
                featuresNavPl, enterpriseNavPl, supportNavPl, pricingNavPl, freeCardPl, maxFreeUsersPl,
                freeEmailSupportPl, freeHelpCenterPl, signUpToAccessPl, proCardPl,
                maxProUsersPl, maxProMemoryLimitPl, proEmailSupportPl, proHelpCenterPl, getStartedPl,
                maxEnterpriseUsersPl, maxEnterpriseMemoryLimitPl, enterpriseEmailSupportPl, enterpriseHelpCenterPl,
                contactUsPl, featuresFooterPl, featuresFooter1Pl, featuresFooter2Pl, featuresFooter3Pl, featuresFooter4Pl,
                featuresFooter5Pl, resourcesFooterPl, resourcesFooter1Pl, resourcesFooter2Pl,
                resourcesFooter3Pl, resourcesFooter4Pl, aboutFooterPl, aboutFooter1Pl, aboutFooter2Pl, aboutFooter3Pl, aboutFooter4Pl
//                ,freeEmailSupportKo, maxFreeMemoryLimitPl, maxEnterpriseUsersKo, maxEnterpriseMemoryLimitKo, enterpriseEmailSupportKo,
//                enterpriseHelpCenterKo,featuresFooterDe, featuresFooter6Pl, enterpriseCardPl, signUpToAccessDe
        ));
    }
}
