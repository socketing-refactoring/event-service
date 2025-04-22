package com.jeein.event.docs.creation;

import static com.jeein.event.docs.util.RestDocsUtil.doc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.ResponseMessage;
import com.jeein.event.docs.constants.ApiPath;
import com.jeein.event.docs.constants.DocumentIdentifier;
import com.jeein.event.docs.snippets.EventSnippet;
import com.jeein.event.dto.request.AreaRequest;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.dto.request.SeatRequest;
import com.jeein.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@DisplayName("이벤트 생성 성공 테스트")
public class EventCreationSuccessTest {

    @Autowired private WebApplicationContext context;

    @Autowired private EventService eventService;

    @Autowired private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(
        WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
            MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .defaultRequest(
                    post("/").accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .build();
    }

    @Test
    @DisplayName("이벤트 요청이 유효하면 이벤트가 성공적으로 생성된다.")
    void createEvent_success() throws Exception {
        // given
        List<AreaRequest> mockAreas =
            IntStream.range(0, 2)
                .mapToObj(i -> AreaRequest.builder()
                    .label(String.valueOf((char) ('A' + i)))
                    .price(50000)
                    .areaMap("<svg>...</svg>")
                    .seats(
                        IntStream.range(0, 2)
                            .mapToObj(j -> SeatRequest.builder()
                                .cx(100 * i + j)
                                .cy(200 * i + j)
                                .row(j / 10 + 1)
                                .number(j % 10 + 1)
                                .build()
                            ).toList()
                    )
                    .build()
                ).toList();

        EventRequest request = EventRequest.of(
            "공연 제목",
            "공연 설명",
            "공연 장소",
            "공연 아티스트",
            Instant.parse("2025-04-25T05:00:00.000Z"),
            Instant.parse("2025-04-30T05:00:00.000Z"),
            "<svg>...</svg>",
            mockAreas,
            List.of(Instant.parse("2025-05-01T05:00:00.000Z"), Instant.parse("2025-05-02T05:00:00.000Z"))
        );

        ClassPathResource file = new ClassPathResource("sample-thumbnail.jpg");
        MockMultipartFile thumbnail = new MockMultipartFile("thumbnail", file.getFilename(), MediaType.IMAGE_JPEG_VALUE, file.getInputStream());

        MockMultipartFile requestPart = new MockMultipartFile("request", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request));

        mockMvc.perform(multipart(ApiPath.EVENT)
                .file(requestPart)
                .file(thumbnail)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("0"))
            .andExpect(jsonPath("$.message").value(ResponseMessage.EVENT_CREATE_SUCCESS))
            .andExpect(jsonPath("$.errors").doesNotExist())
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()))
            .andExpect(jsonPath("$.data.description").value(request.getDescription()))
            .andExpect(jsonPath("$.data.place").value(request.getPlace()))
            .andExpect(jsonPath("$.data.thumbnail").exists())
            .andExpect(jsonPath("$.data.artist").value(request.getArtist()))
            .andExpect(jsonPath("$.data.eventDatetimes").exists())
            .andExpect(jsonPath("$.data.eventDatetimes.length()").value(request.getEventDatetimes().size()))
            .andExpect(jsonPath("$.data.eventOpenTime").exists())
            .andExpect(jsonPath("$.data.ticketingOpenTime").exists())
            .andExpect(jsonPath("$.data.totalMap").value(request.getTotalMap()))
            .andExpect(jsonPath("$.data.eventDatetimes[0].datetime").value(request.getEventDatetimes().get(0).toString()))
            .andExpect(jsonPath("$.data.eventDatetimes[1].datetime").value(request.getEventDatetimes().get(1).toString()))
            .andExpect(jsonPath("$.data.areas[0].label").value(request.getAreas().get(0).getLabel()))
            .andExpect(jsonPath("$.data.areas[0].price").value(request.getAreas().get(0).getPrice()))
            .andExpect(jsonPath("$.data.areas[0].seats[0].row").value(request.getAreas().get(0).getSeats().get(0).getRow()))
            .andExpect(jsonPath("$.data.areas[0].seats[0].number").value(request.getAreas().get(0).getSeats().get(0).getNumber()))
            .andExpect(jsonPath("$.data.areas[1].label").value(request.getAreas().get(1).getLabel()))
            .andExpect(jsonPath("$.data.areas[1].price").value(request.getAreas().get(1).getPrice()))
            .andExpect(jsonPath("$.data.areas[1].seats[1].row").value(request.getAreas().get(1).getSeats().get(1).getRow()))
            .andExpect(jsonPath("$.data.areas[1].seats[1].number").value(request.getAreas().get(1).getSeats().get(1).getNumber()))
            .andExpect(jsonPath("$.data.areas[0].areaMap").value(request.getAreas().get(0).getAreaMap()))
            .andExpect(header().string("Location", containsString("/api/v1/events/")))
            .andDo(
                doc(
                    DocumentIdentifier.EVENT_CREATION_SUCCESS,
                    EventSnippet.EVENT_CREATION_REQUEST_PARTS,
                    EventSnippet.EVENT_CREATION_REQUEST_FIELDS));
//                    responseHeaders(
//                        headerWithName(HttpHeaders.LOCATION)
//                            .description("생성된 리소스의 URI")),
//                    EventSnippet.EVENT_CREATION_RESPONSE_FIELDS));
    }
}
