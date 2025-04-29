package com.jeein.event.docs.retrieval;

import static com.jeein.event.docs.util.RestDocsUtil.doc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jeein.event.ResponseMessage;
import com.jeein.event.data.TestDataFactory;
import com.jeein.event.docs.constants.ApiPath;
import com.jeein.event.docs.constants.DocumentIdentifier;
import com.jeein.event.docs.snippets.EventSnippet;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.entity.Event;
import com.jeein.event.repository.EventRepository;
import com.jeein.event.service.EventService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("단일 공연 조회 실패 테스트")
public class SingleEventRetrievalFailTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;

    private String eventId;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation)).defaultRequest(
                post("/").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .build();

    }

    @BeforeEach
    void setUpEvents() {
        EventRequest eventRequest = TestDataFactory.createEventRequest();
        Event event = eventRepository.save(TestDataFactory.createEvent(eventRequest, "sample-thumbnail.png", UUID.randomUUID().toString()));
        eventId = event.getId().toString();
    }

    @Test
    @DisplayName("공연 아이디가 UUID 형식이 아니면 예외를 던진다.")
    void retrieveSingleEvent_success() throws Exception {


        mockMvc.perform(get(ApiPath.EVENT + "/{eventId}", eventId)).andExpect(status().isOk())
                        .andExpect(jsonPath("$.message")
                                        .value(ResponseMessage.SINGLE_EVENT_RETRIEVAL_FAIL))
                        .andExpect(jsonPath("$.errors").doesNotExist()).andExpect(jsonPath("$.data").exists())
                        .andDo(doc(DocumentIdentifier.SINGLE_EVENT_RETRIEVAL_DETAIL_FAIL,
                                        EventSnippet.SINGLE_EVENT_PATH_PARAMETER,
                                        EventSnippet.SINGLE_EVENT_RETRIEVAL_RESPONSE_FIELDS));
    }

}
