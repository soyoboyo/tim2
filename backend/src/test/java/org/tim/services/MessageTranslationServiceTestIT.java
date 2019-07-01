package org.tim.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.tim.DTOs.MessageDTO;
import org.tim.DTOs.output.MessageWithWarningsDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.entities.Translation;
import org.tim.repositories.MessageRepository;
import org.tim.repositories.TranslationRepository;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTranslationServiceTestIT extends SpringTestsCustomExtension {

    @Autowired
    private  MessageService messageService;

    @Autowired
    private  MessageTranslationService messageTranslationService;

    private Project project;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TranslationRepository translationRepository;


    @BeforeEach
    void setUp(){
        project = createEmptyGermanToEnglishProject();
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenNotCompletedTranslationWithDefinedLocaleExistsThenListOfTranslationsIsReturned(){
        //given
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(!list.isEmpty());
        assertEquals(list.get(0).getContent(), createdMessage.getContent());
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenAllTranslationsForDefinedLocalExistThenEmptyListIsReturned() throws InterruptedException {
        //given
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translation = new Translation();
        translation.setContent("content");
        translation.setMessage(messageFromDB.get());
        translation.setLocale(Locale.ENGLISH);
        Thread.sleep(10L);
        translationRepository.save(translation);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(list.isEmpty());
    }


    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsOutdatedAndDoesNotHaveASubstituteLocaleThenWarningIsReturned() {
        //given
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translation = new Translation();
        translation.setContent("content");
        translation.setLocale(Locale.ENGLISH);
        translation.setMessage(messageFromDB.get());
        translationRepository.save(translation);
        messageFromDB.get().setContent("updated_content");
        messageRepository.save(messageFromDB.get());
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(!list.isEmpty());
        assertEquals(translation.getContent(), list.get(0).getWarnings().getOutdatedTranslationContent());
        assertNull(list.get(0).getWarnings().getSubstituteLocale());
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsOutdatedAndHaveASubstituteLocaleThenWarningIsReturned() {
        //given
        clear();
        project = createEmptyGermanToEnglishProjectWithSubstituteLocales();
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translationEN = new Translation();
        translationEN.setContent("content_en");
        translationEN.setLocale(Locale.ENGLISH);
        translationEN.setMessage(messageFromDB.get());
        translationRepository.save(translationEN);

        messageFromDB.get().setContent("updated_content");
        messageRepository.save(messageFromDB.get());
        messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translationUK = new Translation();
        translationUK.setContent("content_uk");
        translationUK.setLocale(Locale.UK);
        translationUK.setMessage(messageFromDB.get());
        translationRepository.save(translationUK);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(!list.isEmpty());
        assertEquals(translationEN.getContent(), list.get(0).getWarnings().getOutdatedTranslationContent());
        assertEquals(Locale.UK, list.get(0).getWarnings().getSubstituteLocale());
        assertEquals(translationUK.getContent(), list.get(0).getWarnings().getSubstituteContent());
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsMissingAndHaveASubstituteLocaleThenWarningIsReturned() {
        //given
        clear();
        project = createEmptyGermanToEnglishProjectWithSubstituteLocales();
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translationUK = new Translation();
        translationUK.setContent("content_uk");
        translationUK.setLocale(Locale.UK);
        translationUK.setMessage(messageFromDB.get());
        translationRepository.save(translationUK);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(!list.isEmpty());
        assertNull(list.get(0).getWarnings().getOutdatedTranslationContent());
        assertEquals(Locale.UK, list.get(0).getWarnings().getSubstituteLocale());
        assertEquals(translationUK.getContent(), list.get(0).getWarnings().getSubstituteContent());
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsMissingAndDontHaveFirstSubstituteLocaleThenWarningIsReturned() {
        //given
        clear();
        project = createEmptyGermanToEnglishProjectWithTwoSubstituteLocales();
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translationUK = new Translation();
        translationUK.setContent("content_uk");
        translationUK.setLocale(Locale.UK);
        translationUK.setMessage(messageFromDB.get());
        translationRepository.save(translationUK);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslation(project.getId(), Locale.ENGLISH.toString());
        //then
        assertTrue(!list.isEmpty());
        assertNull(list.get(0).getWarnings().getOutdatedTranslationContent());
        assertEquals(Locale.UK, list.get(0).getWarnings().getSubstituteLocale());
        assertEquals(translationUK.getContent(), list.get(0).getWarnings().getSubstituteContent());
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsOutdatedThenTrueIsReturned() throws InterruptedException {
        //given
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Translation translationUK = new Translation();
        translationUK.setContent("content_uk");
        translationUK.setLocale(Locale.UK);
        translationUK.setMessage(messageFromDB.get());
        Translation createdTranslation = translationRepository.save(translationUK);
        Thread.sleep(10L);
        messageFromDB.get().setContent("updated_content");
        messageRepository.save(messageFromDB.get());
        messageFromDB = messageRepository.findById(createdMessage.getId());
        Optional<Translation> translationFromDB = translationRepository.findById(createdTranslation.getId());
        //when
        //then
        assertTrue(messageFromDB.get().isTranslationOutdated(translationFromDB.get()));
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenTranslationIsNotOutdatedThenFalseIsReturned() throws InterruptedException {
        //giving
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        Optional<Message> messageFromDB = messageRepository.findById(createdMessage.getId());
        Thread.sleep(10L);
        Translation translationUK = new Translation();
        translationUK.setContent("content_uk");
        translationUK.setLocale(Locale.UK);
        translationUK.setMessage(messageFromDB.get());
        Translation createdTranslation = translationRepository.save(translationUK);
        Optional<Translation> translationFromDB = translationRepository.findById(createdTranslation.getId());
        //when
        //then
        assertFalse(messageFromDB.get().isTranslationOutdated(translationFromDB.get()));
    }

    @Test
    @WithMockUser(username = "tran", password = "tran")
    void whenMissingTranslationArePresentThenTheyAreReturned(){
        //giving
        clear();
        project = createEmptyGermanToEnglishAndFrenchProject();
        MessageDTO messageDTO = random(MessageDTO.class);
        messageDTO.setProjectId(project.getId());
        Message createdMessage = messageService.createMessage(messageDTO);
        //when
        List<MessageWithWarningsDTO> list = messageTranslationService.getMissingTranslationForProject(project.getId());
        //then
        assertEquals(2, list.size());
        assertEquals(createdMessage.getContent(), list.get(0).getContent());
        assertEquals(createdMessage.getContent(), list.get(1).getContent());
    }

}
