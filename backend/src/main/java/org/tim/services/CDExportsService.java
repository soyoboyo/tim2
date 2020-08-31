package org.tim.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Service;
import org.tim.entities.LocaleWrapper;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.exceptions.EntityNotFoundException;
import org.tim.exceptions.ValidationException;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.ProjectRepository;
import org.tim.repositories.TranslationRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class CDExportsService {

	private final TranslationRepository translationRepository;
	private final ProjectRepository projectRepository;
	private final MessageRepository messageRepository;

	public Map<String, String> exportAllReadyTranslationsByProjectAndByLocale(Long projectId, String locale) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId);
		Map<LocaleWrapper, LocaleWrapper> replaceableLocaleToItsSubstitute = project.getReplaceableLocaleToItsSubstitute();

		Locale orderedLocale;
		try {
			orderedLocale = LocaleUtils.toLocale(locale);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Locale: " + locale + " doesn't exist.");
		}

		List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(orderedLocale, projectId);

		if (translations.isEmpty()) {
			for (Map.Entry<LocaleWrapper, LocaleWrapper> entry : replaceableLocaleToItsSubstitute.entrySet()) {
				if (entry.getKey().getLocale().equals(orderedLocale))
					translations = translationRepository.findTranslationsByLocaleAndProjectId(
							entry.getValue().getLocale(), projectId);
			}
		}

		return translationsWithMessagesToMap(translations, messages);
	}

	public byte[] exportAllReadyTranslationsByProjectInZIP(Long projectId, HttpServletResponse response) throws IOException {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project"));
		response.setHeader("Content-Disposition", "attachment; filename=\"fully-translated_" + project.getName() + "_" + LocalDateTime.now() + ".zip\"");

		List<Message> messages = messageRepository.findMessagesByProjectIdAndIsArchivedFalse(projectId);
		Locale sourceLocale = project.getSourceLocale();
		Set<LocaleWrapper> targetLocales = project.getTargetLocales();

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {

			ObjectMapper mapper = new ObjectMapper();

			zos.putNextEntry(new ZipEntry(sourceLocale.toString() + ".json"));
			zos.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(messagesToMap(messages)));


			for (LocaleWrapper localeWrapper : targetLocales) {
				List<Translation> translations = translationRepository.findTranslationsByLocaleAndProjectId(localeWrapper.getLocale(), projectId);

				if (translations.size() == messages.size()) {
					zos.putNextEntry(new ZipEntry(localeWrapper.getLocale().toString() + ".json"));
					zos.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(translationsToMap(translations)));
				}
			}

			zos.finish();

			return byteArrayOutputStream.toByteArray();
		}
	}

	private Map<String, String> translationsWithMessagesToMap(List<Translation> translations, List<Message> messages) {
		Map<String, String> map = new HashMap<>();

		map.putAll(messagesToMap(messages));
		map.putAll(translationsToMap(translations));

		return map;
	}

	private Map<String, String> messagesToMap(List<Message> messages) {
		Map<String, String> map = new HashMap<>();

		for (Message message : messages) {
			map.put(message.getKey(), message.getContent());
		}

		return map;
	}

	private Map<String, String> translationsToMap(List<Translation> translations) {
		Map<String, String> map = new HashMap<>();

		for (Translation translation : translations) {
			map.put(translation.getMessage().getKey(), translation.getContent());
		}

		return map;
	}
}
