package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;

import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.TranslationRepository;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationSeeder {

    private final TranslationRepository translationRepository;
    public void initTranslations(Map<String, Message> messages, Map<String, Project> projects) {

        Translation translationT1M1 = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("messageM1").getId(), messages.get("messageM1").getProjectId());
        translationT1M1.setContent("Artykuły spożywcze, które pokochasz, idealnie dostarczone.");

        Translation translationT2M1 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM1").getId(), messages.get("messageM1").getProjectId());
        translationT2M1.setContent("Boodschappen waar u van houdt, perfect afgeleverd.");

        Translation translationT3M1 = new Translation(LocaleUtils.toLocale("en_US"), messages.get("messageM1").getId(), messages.get("messageM1").getProjectId());
        translationT3M1.setContent("Groceries you’ll love, perfectly delivered.");

        Translation translationT4M1 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM1").getId(), messages.get("messageM1").getProjectId());
        translationT4M1.setContent("당신이 좋아할 음식, 완벽하게 배달되는 식료품.");

        Translation translationT1M2 = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("messageM2").getId(), messages.get("messageM2").getProjectId());
        translationT1M2.setContent("Niska cena gwarantowana.");

        Translation translationT2M2 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM2").getId(), messages.get("messageM2").getProjectId());
        translationT2M2.setContent("Lage prijsbelofte.");

        Translation translationT3M2 = new Translation(LocaleUtils.toLocale("en_US"), messages.get("messageM2").getId(), messages.get("messageM2").getProjectId());
        translationT3M2.setContent("Low Price Promise.");

        Translation translationT4M2 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM2").getId(), messages.get("messageM2").getProjectId());
        translationT4M2.setContent("저렴한 가격 약속.");

        Translation translationT1M3 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM3").getId(), messages.get("messageM3").getProjectId());
        translationT1M3.setContent("Wir haben ein einzigartiges Geschäftsmodell entwickelt und betreiben, das uns stark positioniert, " +
                "da sich immer mehr Verbraucher für Online-Shopping entscheiden.");

        Translation translationT2M3 = new Translation(LocaleUtils.toLocale("en_GB"), messages.get("messageM3").getId(), messages.get("messageM3").getProjectId());
        translationT2M3.setContent("We have developed and operate a unique business model which positions us strongly" +
                " as more consumers choose to shop online.");

        Translation translationT3M3 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM3").getId(), messages.get("messageM3").getProjectId());
        translationT3M3.setContent("우리는 더 많은 소비자가 온라인 쇼핑을 선택함에 따라 우리를 강하게 위치시키는 독특한 비즈니스 모델을 개발하고 운영합니다.");

        Translation translationT4M3 = new Translation(LocaleUtils.toLocale("ar_LY"), messages.get("messageM3").getId(), messages.get("messageM3").getProjectId());
        translationT4M3.setContent("لقد قمنا بتطوير وتشغيل نموذج أعمال فريد من نوعه يضعنا بقوة مع اختيار المزيد من المستهلكين للتسوق عبر الإنترنت");

        Translation translationT1M4 = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("messageM4").getId(), messages.get("messageM4").getProjectId());
        translationT1M4.setContent("Unser strategisches Ziel ist es, die Interessen unserer Kunden, Investoren und anderer Stakeholder in " +
                "Einklang zu bringen, um einen langfristigen Shareholder Value zu erzielen.");

        Translation translationT2M4 = new Translation(LocaleUtils.toLocale("en_GB"), messages.get("messageM4").getId(), messages.get("messageM4").getProjectId());
        translationT2M4.setContent("Our strategic objective is to align the interests of our customers, " +
                "investors and other stakeholders to deliver long term shareholder value.");

        Translation translationT3M4 = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("messageM4").getId(), messages.get("messageM4").getProjectId());
        translationT3M4.setContent("우리의 전략적 목표는 고객, 투자자 및 기타 이해 관계자의 이익을 조정하여 장기적인 주주 가치를 제공하는 것입니다.");

        Translation translationT4M4 = new Translation(LocaleUtils.toLocale("ar_LY"), messages.get("messageM4").getId(), messages.get("messageM4").getProjectId());
        translationT4M4.setContent("هدفنا الاستراتيجي هو مواءمة مصالح عملائنا والمستثمرين وأصحاب المصلحة الآخرين لتحقيق قيمة طويلة الأجل للمساهمي");


        //all translation needed for "simple web app" presentation
        Translation pricingKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("pricing").getId(), messages.get("pricing").getProjectId());
        pricingKo.setContent("가격");Translation signUpKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("signUp").getId(), messages.get("signUp").getProjectId());
        signUpKo.setContent("가입");Translation ocadoTechnologyKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("ocadoTechnology").getId(), messages.get("ocadoTechnology").getProjectId());
        ocadoTechnologyKo.setContent("Ocado Technology");Translation featuresNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresNav").getId(), messages.get("featuresNav").getProjectId());
        featuresNavKo.setContent("풍모");Translation enterpriseNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseNav").getId(), messages.get("enterpriseNav").getProjectId());
        enterpriseNavKo.setContent("기업");Translation supportNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("supportNav").getId(), messages.get("supportNav").getProjectId());
        supportNavKo.setContent("지원하다");Translation pricingNavKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("pricingNav").getId(), messages.get("pricingNav").getProjectId());
        pricingNavKo.setContent("가격");Translation freeCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeCard").getId(), messages.get("freeCard").getProjectId());
        freeCardKo.setContent("비어 있는");Translation maxFreeUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxFreeUsers").getId(), messages.get("maxFreeUsers").getProjectId());
        maxFreeUsersKo.setContent("10 명의 사용자 포함");Translation maxFreeMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxFreeMemoryLimit").getId(), messages.get("maxFreeMemoryLimit").getProjectId());
        maxFreeMemoryLimitKo.setContent("2GB의 저장 용량");
        //Translation freeEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeEmailSupport"));
        //freeEmailSupportKo.setContent("이메일 지원");
        Translation freeHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeHelpCenter").getId(), messages.get("freeHelpCenter").getProjectId());
        freeHelpCenterKo.setContent("도움말 센터 액세스");Translation signUpToAccessKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("signUpToAccess").getId(), messages.get("signUpToAccess").getProjectId());
        signUpToAccessKo.setContent("무료 가입");Translation proCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proCard").getId(), messages.get("proCard").getProjectId());
        proCardKo.setContent("찬성");Translation maxProUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxProUsers").getId(), messages.get("maxProUsers").getProjectId());
        maxProUsersKo.setContent("사용자 20 명 포함");Translation maxProMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxProMemoryLimit").getId(), messages.get("maxProMemoryLimit").getProjectId());
        maxProMemoryLimitKo.setContent("10GB의 저장 용량");Translation proEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proEmailSupport").getId(), messages.get("proEmailSupport").getProjectId());
        proEmailSupportKo.setContent("이메일 우선 지원");Translation proHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("proHelpCenter").getId(), messages.get("proHelpCenter").getProjectId());
        proHelpCenterKo.setContent("도움말 센터 액세스");Translation getStartedKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("getStarted").getId(), messages.get("getStarted").getProjectId());
        getStartedKo.setContent("시작하다");Translation enterpriseCardKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseCard").getId(), messages.get("enterpriseCard").getProjectId());
        enterpriseCardKo.setContent("기업");
        //Translation maxEnterpriseUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseUsers"));
        //maxEnterpriseUsersKo.setContent("사용자 30 명 포함");Translation maxEnterpriseMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseMemoryLimit"));
        //maxEnterpriseMemoryLimitKo.setContent("15GB의 저장 용량");Translation enterpriseEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseEmailSupport"));
        //enterpriseEmailSupportKo.setContent("전화 및 이메일 지원");Translation enterpriseHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseHelpCenter"));
        //enterpriseHelpCenterKo.setContent("도움말 센터 액세스");
        Translation contactUsKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("contactUs").getId(), messages.get("contactUs").getProjectId());
        contactUsKo.setContent("문의하기");Translation featuresFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter").getId(), messages.get("featuresFooter").getProjectId());
        featuresFooterKo.setContent("풍모");Translation featuresFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter1").getId(), messages.get("featuresFooter1").getProjectId());
        featuresFooter1Ko.setContent("좋은 것");Translation featuresFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter2").getId(), messages.get("featuresFooter2").getProjectId());
        featuresFooter2Ko.setContent("무작위 기능");Translation featuresFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter3").getId(), messages.get("featuresFooter3").getProjectId());
        featuresFooter3Ko.setContent("팀 기능");Translation featuresFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter4").getId(), messages.get("featuresFooter4").getProjectId());
        featuresFooter4Ko.setContent("개발자 용 자료");Translation featuresFooter5Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter5").getId(), messages.get("featuresFooter5").getProjectId());
        featuresFooter5Ko.setContent("다른 것");Translation featuresFooter6Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("featuresFooter6").getId(), messages.get("featuresFooter6").getProjectId());
        featuresFooter6Ko.setContent("마지막으로");Translation resourcesFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter").getId(), messages.get("resourcesFooter").getProjectId());
        resourcesFooterKo.setContent("자원");Translation resourcesFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter1").getId(), messages.get("resourcesFooter1").getProjectId());
        resourcesFooter1Ko.setContent("의지");Translation resourcesFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter2").getId(), messages.get("resourcesFooter2").getProjectId());
        resourcesFooter2Ko.setContent("리소스 이름");Translation resourcesFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter3").getId(), messages.get("resourcesFooter3").getProjectId());
        resourcesFooter3Ko.setContent("다른 리소스");Translation resourcesFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("resourcesFooter4").getId(), messages.get("resourcesFooter4").getProjectId());
        resourcesFooter4Ko.setContent("최종 자원");Translation aboutFooterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter").getId(), messages.get("aboutFooter").getProjectId());
        aboutFooterKo.setContent("약");Translation aboutFooter1Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter1").getId(), messages.get("aboutFooter1").getProjectId());
        aboutFooter1Ko.setContent("팀");Translation aboutFooter2Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter2").getId(), messages.get("aboutFooter2").getProjectId());
        aboutFooter2Ko.setContent("위치");Translation aboutFooter3Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter3").getId(), messages.get("aboutFooter3").getProjectId());
        aboutFooter3Ko.setContent("은둔");Translation aboutFooter4Ko = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("aboutFooter4").getId(), messages.get("aboutFooter4").getProjectId());
        aboutFooter4Ko.setContent("자귀");

        Translation pricingDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("pricing").getId(), messages.get("pricing").getProjectId());
        pricingDe.setContent("Preisgestaltung");Translation signUpDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("signUp").getId(), messages.get("signUp").getProjectId());
        signUpDe.setContent("Anmelden");Translation ocadoTechnologyDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("ocadoTechnology").getId(), messages.get("ocadoTechnology").getProjectId());
        ocadoTechnologyDe.setContent("Ocado Technology");Translation featuresNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresNav").getId(), messages.get("featuresNav").getProjectId());
        featuresNavDe.setContent("Eigenschaften");Translation enterpriseNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseNav").getId(), messages.get("enterpriseNav").getProjectId());
        enterpriseNavDe.setContent("Unternehmen");Translation supportNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("supportNav").getId(), messages.get("supportNav").getProjectId());
        supportNavDe.setContent("Unterstützung");Translation pricingNavDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("pricingNav").getId(), messages.get("pricingNav").getProjectId());
        pricingNavDe.setContent("Preisgestaltung");Translation freeCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeCard").getId(), messages.get("freeCard").getProjectId());
        freeCardDe.setContent("Kostenlos");Translation maxFreeUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxFreeUsers").getId(), messages.get("maxFreeUsers").getProjectId());
        maxFreeUsersDe.setContent("10 Benutzer eingeschlossen");Translation maxFreeMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxFreeMemoryLimit").getId(), messages.get("maxFreeMemoryLimit").getProjectId());
        maxFreeMemoryLimitDe.setContent("2 GB Speicher");Translation freeEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeEmailSupport").getId(), messages.get("freeEmailSupport").getProjectId());
        freeEmailSupportDe.setContent("Email Unterstützung");Translation freeHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("freeHelpCenter").getId(), messages.get("freeHelpCenter").getProjectId());
        freeHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");
        //Translation signUpToAccessDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("signUpToAccess"));
        //signUpToAccessDe.setContent("Melde dich kostenlos an");
        Translation proCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proCard").getId(), messages.get("proCard").getProjectId());
        proCardDe.setContent("Profi");Translation maxProUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxProUsers").getId(), messages.get("maxProUsers").getProjectId());
        maxProUsersDe.setContent("20 Benutzer eingeschlossen");Translation maxProMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxProMemoryLimit").getId(), messages.get("maxProMemoryLimit").getProjectId());
        maxProMemoryLimitDe.setContent("10 GB Speicher");Translation proEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proEmailSupport").getId(), messages.get("proEmailSupport").getProjectId());
        proEmailSupportDe.setContent("Bevorzugte E-Mail-Unterstützung");Translation proHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("proHelpCenter").getId(), messages.get("proHelpCenter").getProjectId());
        proHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");Translation getStartedDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("getStarted").getId(), messages.get("getStarted").getProjectId());
        getStartedDe.setContent("Loslegen");Translation enterpriseCardDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseCard").getId(), messages.get("enterpriseCard").getProjectId());
        enterpriseCardDe.setContent("Unternehmen");Translation maxEnterpriseUsersDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxEnterpriseUsers").getId(), messages.get("maxEnterpriseUsers").getProjectId());
        maxEnterpriseUsersDe.setContent("30 Benutzer eingeschlossen");Translation maxEnterpriseMemoryLimitDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("maxEnterpriseMemoryLimit").getId(), messages.get("maxEnterpriseMemoryLimit").getProjectId());
        maxEnterpriseMemoryLimitDe.setContent("15 GB Speicher");Translation enterpriseEmailSupportDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseEmailSupport").getId(), messages.get("enterpriseEmailSupport").getProjectId());
        enterpriseEmailSupportDe.setContent("Telefon- und E-Mail-Support");Translation enterpriseHelpCenterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("enterpriseHelpCenter").getId(), messages.get("enterpriseHelpCenter").getProjectId());
        enterpriseHelpCenterDe.setContent("Zugriff auf das Hilfezentrum");Translation contactUsDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("contactUs").getId(), messages.get("contactUs").getProjectId());
        contactUsDe.setContent("Kontaktiere uns");
        //Translation featuresFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter"));
        //featuresFooterDe.setContent("Eigenschaften");
        Translation featuresFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter1").getId(), messages.get("featuresFooter1").getProjectId());
        featuresFooter1De.setContent("Cooles Zeug");Translation featuresFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter2").getId(), messages.get("featuresFooter2").getProjectId());
        featuresFooter2De.setContent("Zufälliges Merkmal");Translation featuresFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter3").getId(), messages.get("featuresFooter3").getProjectId());
        featuresFooter3De.setContent("Team-Funktion");Translation featuresFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter4").getId(), messages.get("featuresFooter4").getProjectId());
        featuresFooter4De.setContent("Zeug für Entwickler");Translation featuresFooter5De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter5").getId(), messages.get("featuresFooter5").getProjectId());
        featuresFooter5De.setContent("Noch einer");Translation featuresFooter6De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter6").getId(), messages.get("featuresFooter6").getProjectId());
        featuresFooter6De.setContent("Ostatni raz");Translation resourcesFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter").getId(), messages.get("resourcesFooter").getProjectId());
        resourcesFooterDe.setContent("Ressourcen");Translation resourcesFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter1").getId(), messages.get("resourcesFooter1").getProjectId());
        resourcesFooter1De.setContent("Ressource");Translation resourcesFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter2").getId(), messages.get("resourcesFooter2").getProjectId());
        resourcesFooter2De.setContent("Ressourcenname");Translation resourcesFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter3").getId(), messages.get("resourcesFooter3").getProjectId());
        resourcesFooter3De.setContent("Eine weitere Ressource");Translation resourcesFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("resourcesFooter4").getId(), messages.get("resourcesFooter4").getProjectId());
        resourcesFooter4De.setContent("Letzte Ressource");Translation aboutFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter").getId(), messages.get("aboutFooter").getProjectId());
        aboutFooterDe.setContent("Über");Translation aboutFooter1De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter1").getId(), messages.get("aboutFooter1").getProjectId());
        aboutFooter1De.setContent("Mannschaft");Translation aboutFooter2De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter2").getId(), messages.get("aboutFooter2").getProjectId());
        aboutFooter2De.setContent("Standorte");Translation aboutFooter3De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter3").getId(), messages.get("aboutFooter3").getProjectId());
        aboutFooter3De.setContent("Privatsphäre");Translation aboutFooter4De = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("aboutFooter4").getId(), messages.get("aboutFooter4").getProjectId());
        aboutFooter4De.setContent("Nutzungsbedingungen");

        Translation pricingPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("pricing").getId(), messages.get("pricing").getProjectId());
        pricingPl.setContent("Cennik");Translation signUpPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("signUp").getId(), messages.get("signUp").getProjectId());
        signUpPl.setContent("Zaloguj się");Translation ocadoTechnologyPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("ocadoTechnology").getId(), messages.get("ocadoTechnology").getProjectId());
        ocadoTechnologyPl.setContent("Ocado Technology");Translation featuresNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresNav").getId(), messages.get("featuresNav").getProjectId());
        featuresNavPl.setContent("Funkcje");Translation enterpriseNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseNav").getId(), messages.get("enterpriseNav").getProjectId());
        enterpriseNavPl.setContent("Przedsiębiorstwo");Translation supportNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("supportNav").getId(), messages.get("supportNav").getProjectId());
        supportNavPl.setContent("Wsparcie");Translation pricingNavPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("pricingNav").getId(), messages.get("pricingNav").getProjectId());
        pricingNavPl.setContent("Cennik");Translation freeCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeCard").getId(), messages.get("freeCard").getProjectId());
        freeCardPl.setContent("Darmowy");Translation maxFreeUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxFreeUsers").getId(), messages.get("maxFreeUsers").getProjectId());
        maxFreeUsersPl.setContent("Dołącz 10 użytkowników");
        //Translation maxFreeMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxFreeMemoryLimit"));
        //maxFreeMemoryLimitPl.setContent("2 GB pamięci");
        Translation freeEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeEmailSupport").getId(), messages.get("freeEmailSupport").getProjectId());
        freeEmailSupportPl.setContent("Wsparcie emailowe");Translation freeHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("freeHelpCenter").getId(), messages.get("freeHelpCenter").getProjectId());
        freeHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation signUpToAccessPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("signUpToAccess").getId(), messages.get("signUpToAccess").getProjectId());
        signUpToAccessPl.setContent("Zarejestruj się za darmo");Translation proCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proCard").getId(), messages.get("proCard").getProjectId());
        proCardPl.setContent("Pro");Translation maxProUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxProUsers").getId(), messages.get("maxProUsers").getProjectId());
        maxProUsersPl.setContent("Dołącz 20 użytkowników");Translation maxProMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxProMemoryLimit").getId(), messages.get("maxProMemoryLimit").getProjectId());
        maxProMemoryLimitPl.setContent("10 GB pamięci");Translation proEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proEmailSupport").getId(), messages.get("proEmailSupport").getProjectId());
        proEmailSupportPl.setContent("Priorytetowa obsługa wsparcia e-mail");Translation proHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("proHelpCenter").getId(), messages.get("proHelpCenter").getProjectId());
        proHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation getStartedPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("getStarted").getId(), messages.get("getStarted").getProjectId());
        getStartedPl.setContent("Rozpocznij");
        //Translation enterpriseCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseCard"));
        //enterpriseCardPl.setContent("Przedsiębiorstwo");
        Translation maxEnterpriseUsersPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxEnterpriseUsers").getId(), messages.get("maxEnterpriseUsers").getProjectId());
        maxEnterpriseUsersPl.setContent("Dołącz 30 użytkowników");Translation maxEnterpriseMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxEnterpriseMemoryLimit").getId(), messages.get("maxEnterpriseMemoryLimit").getProjectId());
        maxEnterpriseMemoryLimitPl.setContent("15 GB pamięci");Translation enterpriseEmailSupportPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseEmailSupport").getId(), messages.get("enterpriseEmailSupport").getProjectId());
        enterpriseEmailSupportPl.setContent("Wsparcie telefoniczne i e-mailowe");Translation enterpriseHelpCenterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseHelpCenter").getId(), messages.get("enterpriseHelpCenter").getProjectId());
        enterpriseHelpCenterPl.setContent("Dostęp do centrum pomocy");Translation contactUsPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("contactUs").getId(), messages.get("contactUs").getProjectId());
        contactUsPl.setContent("Skontaktuj się z nami");Translation featuresFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter").getId(), messages.get("featuresFooter").getProjectId());featuresFooterPl.setContent("Funkcje");Translation featuresFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter1").getId(), messages.get("featuresFooter1").getProjectId());
        featuresFooter1Pl.setContent("Fajne rzeczy");Translation featuresFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter2").getId(), messages.get("featuresFooter2").getProjectId());
        featuresFooter2Pl.setContent("Losowa funkcja");Translation featuresFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter3").getId(), messages.get("featuresFooter3").getProjectId());
        featuresFooter3Pl.setContent("Funkcja zespołu");Translation featuresFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter4").getId(), messages.get("featuresFooter4").getProjectId());
        featuresFooter4Pl.setContent("Rzeczy dla programistów");Translation featuresFooter5Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter5").getId(), messages.get("featuresFooter5").getProjectId());
        featuresFooter5Pl.setContent("Inny");
        //Translation featuresFooter6Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter6"));
        //featuresFooter6Pl.setContent("자원");
        Translation resourcesFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter").getId(), messages.get("resourcesFooter").getProjectId());
        resourcesFooterPl.setContent("Zasoby");Translation resourcesFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter1").getId(), messages.get("resourcesFooter1").getProjectId());
        resourcesFooter1Pl.setContent("Zasób");Translation resourcesFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter2").getId(), messages.get("resourcesFooter2").getProjectId());
        resourcesFooter2Pl.setContent("Nazwa zasobu");Translation resourcesFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter3").getId(), messages.get("resourcesFooter3").getProjectId());
        resourcesFooter3Pl.setContent("Inny zasób");Translation resourcesFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("resourcesFooter4").getId(), messages.get("resourcesFooter4").getProjectId());
        resourcesFooter4Pl.setContent("Ostateczny zasób");Translation aboutFooterPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter").getId(), messages.get("aboutFooter").getProjectId());
        aboutFooterPl.setContent("Infornacje");Translation aboutFooter1Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter1").getId(), messages.get("aboutFooter1").getProjectId());
        aboutFooter1Pl.setContent("Zespół");Translation aboutFooter2Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter2").getId(), messages.get("aboutFooter2").getProjectId());
        aboutFooter2Pl.setContent("Lokalizacje");Translation aboutFooter3Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter3").getId(), messages.get("aboutFooter3").getProjectId());
        aboutFooter3Pl.setContent("Prywatność");Translation aboutFooter4Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("aboutFooter4").getId(), messages.get("aboutFooter4").getProjectId());
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
