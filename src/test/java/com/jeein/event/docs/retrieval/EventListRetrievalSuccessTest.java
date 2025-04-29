package com.jeein.event.docs.retrieval;

import static com.jeein.event.docs.util.RestDocsUtil.doc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("공연 목록 조회 테스트")
public class EventListRetrievalSuccessTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation))
                        .defaultRequest(post("/").accept(MediaType.APPLICATION_JSON_VALUE)
                                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .build();

        List<EventRequest> eventRequestList = TestDataFactory.createEventRequestList();
        eventRepository.save(TestDataFactory.createEvent(eventRequestList.get(0), "sample-thumbnail.png", UUID.randomUUID().toString()));
        eventRepository.save(TestDataFactory.createEvent(eventRequestList.get(1), "sample-thumbnail.png", UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("공연 목록을 조회한다.")
    void retrieveEventList_success() throws Exception {
        mockMvc.perform(get(ApiPath.EVENT)).andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value("0"))
                        .andExpect(jsonPath("$.message").value(ResponseMessage.EVENT_RETRIEVAL_SUCCESS))
                        .andExpect(jsonPath("$.errors").doesNotExist())
                        .andExpect(jsonPath("$.data").isArray())
                        .andDo(doc(DocumentIdentifier.EVENT_LIST_RETRIEVAL_SUCCESS,
                                        EventSnippet.EVENT_LIST_RETRIEVAL_RESPONSE_FIELDS));
    }
}
