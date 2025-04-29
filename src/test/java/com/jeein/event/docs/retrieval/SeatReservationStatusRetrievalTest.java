//package com.jeein.event.docs.retrieval;
//
//import static com.jeein.event.docs.util.RestDocsUtil.doc;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jeein.event.ResponseMessage;
//import com.jeein.event.data.TestDataFactory;
//import com.jeein.event.docs.constants.ApiPath;
//import com.jeein.event.docs.constants.DocumentIdentifier;
//import com.jeein.event.docs.snippets.EventSnippet;
//import com.jeein.event.dto.CommonResponse;
//import com.jeein.event.dto.feign.ManagerResponseDTO;
//import com.jeein.event.dto.feign.ReservationResponse;
//import com.jeein.event.dto.request.AreaRequest;
//import com.jeein.event.dto.request.EventRequest;
//import com.jeein.event.dto.request.SeatRequest;
//import com.jeein.event.entity.Event;
//import com.jeein.event.feign.OrderServiceFeignClient;
//import com.jeein.event.repository.EventRepository;
//import com.jeein.event.service.EventService;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.RestDocumentationExtension;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@Transactional
//@ExtendWith(RestDocumentationExtension.class)
//@DisplayName("좌석 예매 현황 조회 성공 테스트")
//public class SeatReservationStatusRetrievalTest {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private EventRepository eventRepository;
//
//    @MockitoBean
//    private OrderServiceFeignClient orderServiceFeignClient;
//
//    private MockMvc mockMvc;
//
//    private String eventId;
//    private String eventDatetimeId;
//
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext,
//        RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//            .apply(documentationConfiguration(restDocumentation)).defaultRequest(
//                post("/").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
//            .build();
//
//    }
//
//    @BeforeEach
//    void setUpEvents() {
//        EventRequest eventRequest = TestDataFactory.createEventRequest();
//        Event event = eventRepository.save(TestDataFactory.createEvent(eventRequest, "sample-thumbnail.png", UUID.randomUUID().toString()));
//        eventId = event.getId().toString();
//        eventDatetimeId = event.getEventDatetimes().getFirst().getId().toString();
//    }
//
//    @Test
//    @DisplayName("특정 일자 공연의 좌석 예매 현황을 조회한다.")
//    void retrieveSingleEvent_success() throws Exception {
//
//        when(orderServiceFeignClient.getReservationsByEventDatetimeId(eventDatetimeId, false)).thenReturn(
//            ResponseEntity.ok(CommonResponse.success("좌석 주문 정보 조회 성공", "0", new ArrayList<>()))
//        );
//
//        mockMvc.perform(get(ApiPath.EVENT + "/{eventId}/dates/{eventDatetimeId}/seat-reservation", eventId, eventDatetimeId))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.code").value("0"))
//            .andExpect(jsonPath("$.message")
//                .value(ResponseMessage.SEAT_RESERVATION_STATUS_LIST_RETRIEVAL_SUCCESS))
//            .andExpect(jsonPath("$.errors").doesNotExist()).andExpect(jsonPath("$.data").exists())
//            .andDo(doc(DocumentIdentifier.SEAT_RESERVATION_STATUS_LIST_RETRIEVAL_SUCCESS,
//                EventSnippet.SINGLE_EVENTDATETIME_PATH_PARAMETER,
//                EventSnippet.SEAT_RESERVATION_STATUS_LIST_RETRIEVAL_RESPONSE));
//    }
//
//}
