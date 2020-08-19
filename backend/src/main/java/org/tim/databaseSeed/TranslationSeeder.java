package org.tim.databaseSeed;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.entities.Message;
import org.tim.entities.Translation;
import org.tim.repositories.TranslationRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TranslationSeeder {

	private final TranslationRepository translationRepository;

	public void initTranslations(Map<String, Message> messages) {

		LinkedList<Translation> translations = new LinkedList<>();
		List<String> projectIds = Arrays.asList("P1", "P2", "P3", "P4");

		for (String id : projectIds) {
			String path = "project" + id + "/translations" + id + ".json";
			ArrayList<LinkedHashMap<String, Object>> messagesArray = SeederUtils.getObjectsFromJSON(path);

			for (LinkedHashMap<String, Object> m : messagesArray) {
				String messageKey = (String) m.get("messageKey");
				List<LinkedHashMap<String, Object>> translationsForMessage = (List<LinkedHashMap<String, Object>>) m.get("translations");
				for (LinkedHashMap<String, Object> t : translationsForMessage) {
					String stringLocale = (String) t.get("locale");
					String content = (String) t.get("content");
					if (content != null) {
						Translation translation = new Translation(LocaleUtils.toLocale(stringLocale), messages.get(messageKey));
						translation.setContent(content);
						translations.add(translation);
					}
				}
			}
		}
		translationRepository.saveAll(translations);
		// project 2

		// de_DE
		//Translation signUpToAccessDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("signUpToAccess"));
		//signUpToAccessDe"content": "Melde dich kostenlos an");
		//Translation featuresFooterDe = new Translation(LocaleUtils.toLocale("de_DE"), messages.get("featuresFooter"));
		//featuresFooterDe"content": "Eigenschaften");

		// ko_KR
		//Translation maxEnterpriseUsersKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseUsers"));
		//maxEnterpriseUsersKo"content": "사용자 30 명 포함");Translation maxEnterpriseMemoryLimitKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("maxEnterpriseMemoryLimit"));
		//maxEnterpriseMemoryLimitKo"content": "15GB의 저장 용량");Translation enterpriseEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseEmailSupport"));
		//enterpriseEmailSupportKo"content": "전화 및 이메일 지원");Translation enterpriseHelpCenterKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("enterpriseHelpCenter"));
		//enterpriseHelpCenterKo"content": "도움말 센터 액세스");
		//Translation freeEmailSupportKo = new Translation(LocaleUtils.toLocale("ko_KR"), messages.get("freeEmailSupport"));
		//freeEmailSupportKo"content": "이메일 지원");
		//Translation featuresFooter6Pl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("featuresFooter6"));
		//featuresFooter6Pl"content": "자원");


		// pl_PL
		//Translation maxFreeMemoryLimitPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("maxFreeMemoryLimit"));
		//maxFreeMemoryLimitPl"content": "2 GB pamięci");
		//Translation enterpriseCardPl = new Translation(LocaleUtils.toLocale("pl_PL"), messages.get("enterpriseCard"));
		//enterpriseCardPl"content": "Przedsiębiorstwo");

	}
}
