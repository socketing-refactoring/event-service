package com.jeein.event.docs.deletion;

import static com.jeein.event.docs.util.RestDocsUtil.doc;
import static com.jeein.event.docs.util.RestDocsUtil.multipart_doc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.ResponseMessage;
import com.jeein.event.docs.constants.ApiPath;
import com.jeein.event.docs.constants.DocumentIdentifier;
import com.jeein.event.docs.snippets.CommonSnippet;
import com.jeein.event.docs.snippets.EventSnippet;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.SeatRequest;
import com.jeein.event.service.EventService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("공연 소프트 삭제 테스트")
public class EventSoftDeletionSuccessTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Value("${upload.path}")
    private String uploadPath;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .defaultRequest(post("/").accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .build();
    }

//    @AfterEach
//    void cleanUpUploadedFiles() throws IOException, InterruptedException {
//
//        Path uploadDirectory = Paths.get(uploadPath);
//        if (Files.exists(uploadDirectory)) {
//            try (Stream<Path> paths = Files.walk(uploadDirectory)) {
//                paths.sorted(Comparator.reverseOrder())
//                    .forEach(path -> {
//                        try {
//                            Files.delete(path);
//                        } catch (IOException e) {
//                            System.err.println("파일 삭제 실패: " + path);
//                        }
//                    });
//            }
//        } else {
//            System.out.println("삭제할 디렉토리가 존재하지 않음: " + uploadDirectory);
//        }
//    }

    @Test
    @DisplayName("공연를 소프트 삭제한다.")
    void retrieveEventList_success() throws Exception {

        // given
        List<AreaRequest> mockAreas = IntStream.range(0, 2).mapToObj(i -> AreaRequest.of(
                String.valueOf((char) ('A' + i)), 50000, "<svg>...</svg>",
                IntStream.range(0, 2).mapToObj(
                        j -> SeatRequest.of(100 * i + j, 200 * i + j, j / 10 + 1, j % 10 + 1))
                    .toList()))
            .toList();
        EventRequest request = EventRequest.of("공연 제목", "공연 설명", "공연 장소", "공연 아티스트",
            Instant.parse("2025-04-25T05:00:00.000Z"), Instant.parse("2025-04-30T05:00:00.000Z"),
            "<svg>...</svg>", mockAreas, List.of(Instant.parse("2025-05-01T05:00:00.000Z"),
                Instant.parse("2025-05-02T05:00:00.000Z")));

        MockMultipartFile requestPart = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request));
        ClassPathResource file = new ClassPathResource("sample-thumbnail.jpg");
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", file.getFilename(),
            MediaType.IMAGE_JPEG_VALUE, file.getInputStream());

        // when
        String responseJson = mockMvc.perform(multipart(ApiPath.EVENT).file(requestPart).file(thumbnail))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String eventId = objectMapper.readTree(responseJson).path("data").path("id").asText();

        //then
        mockMvc.perform(delete(ApiPath.EVENT + "/{eventId}", eventId))
            .andExpect(status().isNoContent()).andExpect(jsonPath("$.code").value("0"))
            .andExpect(jsonPath("$.message").value(ResponseMessage.EVENT_DELETION_SUCCESS))
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(doc(DocumentIdentifier.EVENT_DELETION_SUCCESS,
                EventSnippet.EVENT_DELETION_REQUEST_PATH_PARAMETER,
                CommonSnippet.successCodeOnlyResponseFields()));
        }
}
