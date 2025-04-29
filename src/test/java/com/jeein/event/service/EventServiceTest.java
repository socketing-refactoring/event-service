package com.jeein.event.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.data.TestDataFactory;
import com.jeein.event.dto.request.EventRequest;
import com.jeein.event.entity.Event;
import com.jeein.event.exception.CustomFeignException;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.EventException;
import com.jeein.event.exception.UploadException;
import com.jeein.event.feign.MemberServiceFeignClient;
import com.jeein.event.repository.EventRepository;
import com.jeein.event.util.UploadManager;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Event Service 테스트")
public class EventServiceTest {

    @Mock
    private MemberServiceFeignClient memberServiceFeignClient;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UploadManager uploadManager;

    @InjectMocks
    private EventService eventService;

    private EventRequest eventRequest;

    private MockMultipartFile thumbnail;
    private String managerId;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        managerId = UUID.randomUUID().toString();
        eventRequest = TestDataFactory.createEventRequest();
        thumbnail = TestDataFactory.createThumbnailMockMultipartFile();
    }

    @Test
    @DisplayName("매니저 Feign 클라이언트에서 에러 응답을 받으면 EventException 예외를 던진다.")
    void testGetOneManager_ExceptionThrown() {

        CustomFeignException customFeignException = new CustomFeignException("M_01", "매니저 조회에 실패했습니다.", null);
        when(memberServiceFeignClient.getOneManager(managerId)).thenThrow(customFeignException);

        EventException exception = assertThrows(EventException.class, () -> {
            eventService.saveEvent(eventRequest, thumbnail, managerId);
        });
        log.info("Event Exception message: {}", exception.getMessage());
        assertEquals(ErrorCode.INVALID_TOKEN, exception.getErrorCode());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("이미 존재하는 공연 제목을 가진 공연 생성을 시도하면 예외를 던진다.")
    void testSaveEvent_EventTitleAlreadyExists() {
        // Arrange
        when(eventRepository.findByTitle(eventRequest.getTitle()))
                        .thenReturn(Optional.ofNullable(mock(Event.class)));

        // Act & Assert
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.saveEvent(eventRequest, thumbnail, managerId);
        });

        log.debug("Exception message: {}", exception.getMessage());
        assertEquals(ErrorCode.EVENT_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    @DisplayName("유효하지 않은 이미지 데이터")
    void testSaveEvent_FileUploadFailure() throws IOException {
        // Arrange
        when(uploadManager.uploadFile(thumbnail, eventRequest.getTitle()))
                        .thenThrow(new MultipartException("Invalid file"));

        // Act & Assert
        EventException exception = assertThrows(EventException.class, () -> {
            eventService.saveEvent(eventRequest, thumbnail, managerId);
        });
        assertEquals(ErrorCode.INVALID_MULTIPARTFILE, exception.getErrorCode());
    }

    @Test
    void testSaveEvent_FileUploadIOException() throws IOException {
        // Arrange
        when(uploadManager.uploadFile(thumbnail, eventRequest.getTitle()))
                        .thenThrow(new IOException("Upload failed"));

        // Act & Assert
        UploadException exception = assertThrows(UploadException.class, () -> {
            eventService.saveEvent(eventRequest, thumbnail, managerId);
        });
        assertEquals(ErrorCode.UPLOAD_ERROR, exception.getErrorCode());
    }
}
