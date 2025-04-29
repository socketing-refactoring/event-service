package com.jeein.event.docs.retrieval;

import static com.jeein.event.docs.util.RestDocsUtil.doc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.ResponseMessage;
import com.jeein.event.data.TestDataFactory;
import com.jeein.event.docs.constants.ApiPath;
import com.jeein.event.docs.constants.DocumentIdentifier;
import com.jeein.event.docs.snippets.EventSnippet;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.SeatRequest;
import com.jeein.event.entity.Event;
import com.jeein.event.repository.EventRepository;
import com.jeein.event.service.EventService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("좌석 목록 조회 테스트")
public class SeatListRetrievalSuccessTest {

    @Autowired
    private WebApplicationContext context;

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
    @DisplayName("단일 공연의 좌석 목록을 조회한다.")
    void retrieveSingleEvent_success() throws Exception {


        mockMvc.perform(get(ApiPath.EVENT + "/{eventId}" + "/seats", eventId)).andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("0"))
            .andExpect(jsonPath("$.message")
                .value(ResponseMessage.SEAT_LIST_RETRIEVAL_SUCCESS))
            .andExpect(jsonPath("$.errors").doesNotExist()).andExpect(jsonPath("$.data").exists())
            .andDo(doc(DocumentIdentifier.SEAT_LIST_RETRIEVAL_SUCCESS,
                EventSnippet.SINGLE_EVENT_PATH_PARAMETER,
                EventSnippet.SEAT_LIST_RETRIEVAL_RESPONSE));
    }
}
