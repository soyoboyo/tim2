package org.tim.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tim.DTOs.MessageDTO;
import org.tim.configuration.SpringTestsCustomExtension;
import org.tim.entities.Message;
import org.tim.entities.Project;
import org.tim.services.MessageService;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tim.utils.Mapping.*;

public class MessageControllerTestIT extends SpringTestsCustomExtension {

    private MockMvc mockMvc;
    private static ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    private final String BASE_URL = "http://localhost:8081";

    private Message message;

    private Project project;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        project = createEmptyGermanToEnglishProject();
        message = random(Message.class);
        message.setProject(project);
    }

    @Test
    public void whenValidMessageIsGivingForCreationThenMessageGoesToService() throws Exception {
        //given
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setProjectId(project.getId());
        messageDTO.setContent("content");
        messageDTO.setKey("key");
        String jsonRequest = mapper.writeValueAsString(messageDTO);
        when(messageService.createMessage(messageDTO)).thenReturn(message);
        //when
        mockMvc.perform(post(BASE_URL + API_VERSION + MESSAGE + CREATE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                //then
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(message.getContent())))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidMessageIsGivingForCreationThenBadRequestIsReturned() throws Exception {
        //given
        MessageDTO messageDTO = new MessageDTO();
        String jsonRequest = mapper.writeValueAsString(messageDTO);
        //when
        mockMvc.perform(post(BASE_URL + API_VERSION + MESSAGE + CREATE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                //then
                .andExpect(status().isBadRequest());
    }


    @Test
    public void whenValidMessageIsGivingForUpdateThenMessageGoesToService() throws Exception {
        //given
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setProjectId(project.getId());
        messageDTO.setContent("content");
        messageDTO.setKey("key");
        String jsonRequest = mapper.writeValueAsString(messageDTO);
        message.setContent("updated_content");
        when(messageService.updateMessage(messageDTO, message.getId())).thenReturn(message);
        //when
        mockMvc.perform(post(BASE_URL + API_VERSION + MESSAGE + "/update/" + message.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                //then
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("updated_content")))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidMessageIsGivingForUpdateThenBadRequestIsReturned() throws Exception {
        //given
        MessageDTO messageDTO = new MessageDTO();
        String jsonRequest = mapper.writeValueAsString(messageDTO);
        when(messageService.updateMessage(messageDTO, message.getId())).thenReturn(message);
        //when
        mockMvc.perform(post(BASE_URL + API_VERSION + MESSAGE + "/update/" + message.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenMessageIsGivingForRemovalThenItGoesToService() throws Exception {
        //given
        String messageId = "ed";
        when(messageService.archiveMessage(messageId)).thenReturn(message);
        //when
        mockMvc.perform(delete(BASE_URL + API_VERSION + MESSAGE + "/archive/" + messageId))
                //then
                .andExpect(status().isOk());
    }
}
