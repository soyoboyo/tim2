package org.tim.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tim.entities.TranslationVersion;
import org.tim.repositories.TranslationVersionRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TranslationVersionService {

    private final TranslationVersionRepository translationVersionRepository;

    public List<TranslationVersion> getTranslationVersionsByOriginal(String originalId){
        return translationVersionRepository.findAllByTranslationIdOrderByUpdateDateDesc(originalId);
    }
}
