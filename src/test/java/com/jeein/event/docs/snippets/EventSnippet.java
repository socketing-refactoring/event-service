package com.jeein.event.docs.snippets;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.RequestPartFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestPartsSnippet;

public class EventSnippet {

    public static final RequestHeadersSnippet EVENT_CREATION_REQUEST_HEADERS = requestHeaders(
                    headerWithName("Authorization").description("Bearer token for authentication"),
                    headerWithName("Content-Type").description("The MIME type of the body of the request"));

    public static final RequestPartsSnippet EVENT_CREATION_REQUEST_PARTS =
                    requestParts(partWithName("request").description("공연 정보(JSON)"),
                                    partWithName("thumbnail").description("공연 포스터 이미지 파일 (Multipart)"));
    public static final RequestPartFieldsSnippet EVENT_CREATION_REQUEST_FIELDS = requestPartFields("request",
                    fieldWithPath("title").description("공연 제목")
                                    .attributes(key("constraint").value("공백 불가, 최대 20자")),
                    fieldWithPath("description").description("공연 설명")
                                    .attributes(key("constraint").value("공백 불가, 최대 100자")),
                    fieldWithPath("place").description("공연 장소")
                                    .attributes(key("constraint").value("공백 불가, 최대 20자")),
                    fieldWithPath("artist").description("공연 아티스트")
                                    .attributes(key("constraint").value("공백 불가, 최대 10자")),
                    fieldWithPath("eventOpenTime").description("공연 오픈 일정")
                                    .attributes(key("constraint").value("공백 불가, ISO 8601")),
                    fieldWithPath("ticketingOpenTime").description("티켓팅 오픈 일정")
                                    .attributes(key("constraint").value("공백 불가, ISO 8601")),
                    fieldWithPath("totalMap").description("좌석 배치도 이미지 경로")
                                    .attributes(key("constraint").value("공백 불가")),
                    fieldWithPath("areas").description("공연 구역 리스트")
                                    .attributes(key("constraint").value("공백 불가")),
                    fieldWithPath("areas[].label").description("구역 이름")
                                    .attributes(key("constraint").value("공백 불가")),
                    fieldWithPath("areas[].price").description("가격")
                                    .attributes(key("constraint").value("공백 불가")),
                    fieldWithPath("areas[].areaMap").description("구역 지도 SVG")
                                    .attributes(key("constraint").value("공백 불가")),
                    fieldWithPath("areas[].seats[].cx").description("좌표 X"),
                    fieldWithPath("areas[].seats[].cy").description("좌표 Y"),
                    fieldWithPath("areas[].seats[].row").description("좌석 행"),
                    fieldWithPath("areas[].seats[].number").description("좌석 번호"),
                    fieldWithPath("eventDatetimes[]").description("공연 일정 리스트 (ISO 8601)"));
    public static final ResponseFieldsSnippet EVENT_CREATION_RESPONSE_FIELDS =
                    CommonSnippet.successResponseFields().and(fieldWithPath("data.id").description("이벤트 ID"),
                                    fieldWithPath("data.title").description("공연 제목"),
                                    fieldWithPath("data.description").description("공연 설명"),
                                    fieldWithPath("data.place").description("공연 장소"),
                                    fieldWithPath("data.thumbnail").description("포스터 이미지 URL"),
                                    fieldWithPath("data.artist").description("공연 아티스트"),
                                    fieldWithPath("data.eventOpenTime").description("공연 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data.ticketingOpenTime")
                                                    .description("티켓팅 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data.totalMap").description("전체 좌석 배치도 URL"),
                                    fieldWithPath("data.eventDatetimes").description("공연 일정 목록"),
                                    fieldWithPath("data.eventDatetimes[].id").description("공연 일정 ID"),
                                    fieldWithPath("data.eventDatetimes[].datetime")
                                                    .description("공연 일시 (ISO 8601)"),
                                    fieldWithPath("data.areas").description("공연 구역 목록"),
                                    fieldWithPath("data.areas[].id").description("구역 아이디"),
                                    fieldWithPath("data.areas[].eventId").description("해당 구역의 공연 아이디"),
                                    fieldWithPath("data.areas[].label").description("구역 이름"),
                                    fieldWithPath("data.areas[].price").description("구역 가격"),
                                    fieldWithPath("data.areas[].areaMap").description("구역 배치도 SVG"),
                                    fieldWithPath("data.areas[].seats").description("좌석 목록"),
                                    fieldWithPath("data.areas[].seats[].id").description("좌석 아이디"),
                                    fieldWithPath("data.areas[].seats[].areaId").description("해당 좌석의 구역 아이디"),
                                    fieldWithPath("data.areas[].seats[].cx").description("좌석 X 좌표"),
                                    fieldWithPath("data.areas[].seats[].cy").description("좌석 Y 좌표"),
                                    fieldWithPath("data.areas[].seats[].row").description("좌석 행 번호"),
                                    fieldWithPath("data.areas[].seats[].number").description("좌석 번호"));

    public static final ResponseFieldsSnippet EVENT_LIST_RETRIEVAL_RESPONSE_FIELDS =
                    CommonSnippet.successResponseFields().and(fieldWithPath("data").description("이벤트 목록"),
                                    fieldWithPath("data[].id").description("이벤트 ID"),
                                    fieldWithPath("data[].title").description("공연 제목"),
                                    fieldWithPath("data[].description").description("공연 설명"),
                                    fieldWithPath("data[].place").description("공연 장소"),
                                    fieldWithPath("data[].thumbnail").description("포스터 이미지 URL"),
                                    fieldWithPath("data[].artist").description("공연 아티스트"),
                                    fieldWithPath("data[].eventOpenTime").description("공연 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data[].ticketingOpenTime")
                                                    .description("티켓팅 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data[].eventDatetimes").description("공연 일정 목록"),
                                    fieldWithPath("data[].eventDatetimes[].id").description("공연 일정 ID"),
                                    fieldWithPath("data[].eventDatetimes[].datetime")
                                                    .description("공연 일시 (ISO 8601)"));

    public static final PathParametersSnippet SINGLE_EVENT_PATH_PARAMETER =
                    pathParameters(parameterWithName("eventId").description("조회하고 싶은 공연 아이디"));

    public static final ResponseFieldsSnippet SINGLE_EVENT_RETRIEVAL_RESPONSE_FIELDS =
                    CommonSnippet.successResponseFields().and(fieldWithPath("data").description("이벤트 목록"),
                                    fieldWithPath("data.id").description("이벤트 ID"),
                                    fieldWithPath("data.title").description("공연 제목"),
                                    fieldWithPath("data.description").description("공연 설명"),
                                    fieldWithPath("data.place").description("공연 장소"),
                                    fieldWithPath("data.thumbnail").description("포스터 이미지 URL"),
                                    fieldWithPath("data.artist").description("공연 아티스트"),
                                    fieldWithPath("data.eventOpenTime").description("공연 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data.ticketingOpenTime")
                                                    .description("티켓팅 오픈 일정 (ISO 8601)"),
                                    fieldWithPath("data.eventDatetimes").description("공연 일정 목록"),
                                    fieldWithPath("data.eventDatetimes[].id").description("공연 일정 ID"),
                                    fieldWithPath("data.eventDatetimes[].datetime")
                                                    .description("공연 일시 (ISO 8601)"));

    public static final PathParametersSnippet EVENT_DELETION_REQUEST_PATH_PARAMETER =
                    pathParameters(parameterWithName("eventId").description("삭제하고 싶은 공연 아이디"));
}
