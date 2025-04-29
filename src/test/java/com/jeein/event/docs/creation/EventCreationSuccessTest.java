package com.jeein.event.docs.creation;

import static com.jeein.event.docs.util.RestDocsUtil.multipart_doc;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jeein.event.ResponseMessage;
import com.jeein.event.data.TestDataFactory;
import com.jeein.event.docs.constants.ApiPath;
import com.jeein.event.docs.constants.DocumentIdentifier;
import com.jeein.event.docs.snippets.EventSnippet;
import com.jeein.event.dto.CommonResponse;
import com.jeein.event.dto.feign.ManagerResponseDTO;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.feign.MemberServiceFeignClient;
import com.jeein.event.service.EventService;
import com.jeein.event.util.UploadManager;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("공연 생성 성공 테스트")
public class EventCreationSuccessTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventService eventService;

    @MockitoBean
    private MemberServiceFeignClient memberServiceFeignClient;

    @MockitoBean
    private UploadManager uploadManager;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation))
                        .defaultRequest(post("/").accept(MediaType.APPLICATION_JSON_VALUE)
                                        .contentType(MediaType.MULTIPART_FORM_DATA))
                        .build();
    }

    @Test
    @DisplayName("공연 요청이 유효하면 공연가 성공적으로 생성된다.")
    void createEvent_success() throws Exception {

        // given
        EventRequest request = TestDataFactory.createEventRequest();
        MockMultipartFile requestPart = TestDataFactory.createMockMultipartFile(request);
        MockMultipartFile thumbnail = TestDataFactory.createThumbnailMockMultipartFile();
        String managerId = UUID.randomUUID().toString();

        when(memberServiceFeignClient.getOneManager(managerId)).thenReturn(ResponseEntity.ok(
                        CommonResponse.success("매니저 유효성 검사 성공", "Success", mock(ManagerResponseDTO.class))));

        when(uploadManager.uploadFile(any(), any())).thenReturn("filename");

        // when & then
        mockMvc.perform(multipart(ApiPath.EVENT).file(requestPart).file(thumbnail).header("x-api-managerId",
                        managerId)).andDo(print()).andExpect(status().isCreated())
                        .andExpect(jsonPath("$.code").value("0"))
                        .andExpect(jsonPath("$.message").value(ResponseMessage.EVENT_CREATION_SUCCESS))
                        .andExpect(jsonPath("$.errors").doesNotExist())
                        .andExpect(jsonPath("$.data.id").exists())
                        .andExpect(jsonPath("$.data.title").value(request.getTitle()))
                        .andExpect(jsonPath("$.data.description").value(request.getDescription()))
                        .andExpect(jsonPath("$.data.place").value(request.getPlace()))
                        .andExpect(jsonPath("$.data.thumbnail").exists())
                        .andExpect(jsonPath("$.data.artist").value(request.getArtist()))
                        .andExpect(jsonPath("$.data.eventOpenTime").exists())
                        .andExpect(jsonPath("$.data.ticketingOpenTime").exists())
                        .andExpect(jsonPath("$.data.eventDatetimes").isArray())
                        .andExpect(jsonPath("$.data.totalMap").value(request.getTotalMap()))
                        .andExpect(jsonPath("$.data.areas").isArray())
                        .andExpect(header().string("Location", containsString("/api/v1/events/")))
                        .andDo(multipart_doc(DocumentIdentifier.EVENT_CREATION_SUCCESS,
                                        EventSnippet.EVENT_CREATION_REQUEST_PARTS,
                                        EventSnippet.EVENT_CREATION_REQUEST_FIELDS,
                                        responseHeaders(headerWithName(HttpHeaders.LOCATION)
                                                        .description("생성된 리소스의 URI")),
                                        EventSnippet.EVENT_CREATION_RESPONSE_FIELDS));
    }
}
