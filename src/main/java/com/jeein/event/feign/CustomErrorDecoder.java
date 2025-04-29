package com.jeein.event.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeein.event.dto.CommonResponse;
import com.jeein.event.exception.CustomFeignException;
import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.FeignClientParseException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            CommonResponse<?> responseBody = parse(response);
            return new CustomFeignException(responseBody.getCode(), responseBody.getMessage(),
                            responseBody.getData());
        } catch (Exception e) {
            return new ErrorDecoder.Default().decode(methodKey, response);
        }
    }

    private CommonResponse<?> parse(Response response) {
        try {
            return objectMapper.readValue(response.body().asInputStream(), CommonResponse.class);
        } catch (Exception e) {
            log.debug(e.getMessage());
            log.debug(e.getCause().getMessage());
            throw new FeignClientParseException(ErrorCode.FEIGN_PARSE_ERROR);
        }
    }
}
