package org.tim.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tim.DTOs.output.MessageForTranslator;
import org.tim.DTOs.output.TranslationForTranslator;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.exceptions.ValidationException;
import org.tim.services.MessageForTranslatorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.constants.Mappings.*;

public class MessageForTranslatorControllerTestIT extends SpringTestsCustomExtension {

    private MockMvc mockMvc;

    @InjectMocks
    private MessageForTranslatorController messageForTranslatorController;

    @Mock
    private MessageForTranslatorService messageForTranslatorService;

    private final String BASE_URL = "http://localhost:8081";

    private List<MessageForTranslator> messageForTranslatorList;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageForTranslatorController).build();
        messageForTranslatorList = new ArrayList<>();
        MessageForTranslator messageForTranslator = new MessageForTranslator();
        messageForTranslator.setId(1L);
        messageForTranslator.setKey("key");
        messageForTranslator.setContent("content");
        messageForTranslatorList.add(messageForTranslator);
    }

    @Test
    void whenGetMessageForTranslationWithoutLocaleThenReturnMessagesWithoutTranslationAndSubstitute() throws Exception {
        when(messageForTranslatorService.getMessagesForTranslator(any())).thenReturn(messageForTranslatorList);
        mockMvc.perform(get(BASE_URL + API_VERSION + MESSAGE + TRANSLATOR + "/getByProject/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].key", Matchers.is("key")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute", Matchers.nullValue()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetMessageAndTranslationAndSubstituteNotExistThenReturnMessagesWithoutTranslationAndSubstitute() throws Exception {
        when(messageForTranslatorService.getMessagesForTranslator(any(), any())).thenReturn(messageForTranslatorList);
        mockMvc.perform(get(BASE_URL + API_VERSION + MESSAGE + TRANSLATOR + "/getByLocale/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("locale", "pl_PL"))
                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].key", Matchers.is("key")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute", Matchers.nullValue()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetMessageAndTranslationNotExistButSubstituteExistThenReturnOnlySubstitute() throws Exception {
        TranslationForTranslator translationForTranslator = new TranslationForTranslator();
        translationForTranslator.setId(1L);
        translationForTranslator.setContent("content");
        translationForTranslator.setIsValid(true);
        translationForTranslator.setLocale("en_US");
        translationForTranslator.setMessageId(1L);
        messageForTranslatorList.get(0).setSubstitute(translationForTranslator);
        when(messageForTranslatorService.getMessagesForTranslator(any(), any())).thenReturn(messageForTranslatorList);
        mockMvc.perform(get(BASE_URL + API_VERSION + MESSAGE + TRANSLATOR + "/getByLocale/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("locale", "pl_PL"))
                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].key", Matchers.is("key")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation", Matchers.nullValue()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.isValid", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.locale", Matchers.is("en_US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.messageId", Matchers.is(1)))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetMessageAndTranslationExistAndSubstituteExistThenReturnTranslationAdnSubstitute() throws Exception {
        TranslationForTranslator translationForTranslator = new TranslationForTranslator();
        translationForTranslator.setId(1L);
        translationForTranslator.setContent("content");
        translationForTranslator.setIsValid(true);
        translationForTranslator.setLocale("en_US");
        translationForTranslator.setMessageId(1L);
        messageForTranslatorList.get(0).setSubstitute(translationForTranslator);

        translationForTranslator = new TranslationForTranslator();
        translationForTranslator.setId(2L);
        translationForTranslator.setContent("org_content");
        translationForTranslator.setIsValid(true);
        translationForTranslator.setLocale("en_UK");
        translationForTranslator.setMessageId(1L);
        messageForTranslatorList.get(0).setTranslation(translationForTranslator);

        when(messageForTranslatorService.getMessagesForTranslator(any(), any())).thenReturn(messageForTranslatorList);
        mockMvc.perform(get(BASE_URL + API_VERSION + MESSAGE + TRANSLATOR + "/getByLocale/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("locale", "pl_PL"))
                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].key", Matchers.is("key")))

                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation.id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation.content", Matchers.is("org_content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation.isValid", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation.locale", Matchers.is("en_UK")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].translation.messageId", Matchers.is(1)))

                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.content", Matchers.is("content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.isValid", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.locale", Matchers.is("en_US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].substitute.messageId", Matchers.is(1)))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetMessageAndLocaleSetWithWrongFormatThrowValidationException() throws Exception {
        String wrongLocale = "wrong_format";
        when(messageForTranslatorService.getMessagesForTranslator(any(), any())).thenThrow(
                new ValidationException("Source locale: " + wrongLocale +" was given in the wrong format."));
        mockMvc.perform(get(BASE_URL + API_VERSION + MESSAGE + TRANSLATOR + "/getByLocale/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("locale", wrongLocale))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
