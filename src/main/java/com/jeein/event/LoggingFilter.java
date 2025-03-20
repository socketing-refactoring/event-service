package com.jeein.event;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final String SWAGGER_PATH = "/swagger";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isSwaggerRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        byte[] requestBody = requestWrapper.getContentAsByteArray();
        log.info("request : " + new String(requestBody, StandardCharsets.UTF_8));

        byte[] responseBody = responseWrapper.getContentAsByteArray();
        log.info("response : " + new String(responseBody, StandardCharsets.UTF_8));

        responseWrapper.copyBodyToResponse();
    }

    // Helper method to check if the request is for Swagger endpoints
    private boolean isSwaggerRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains(SWAGGER_PATH);
    }
}
