package com.jeein.event.docs.util;

import java.util.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.OperationRequestPart;

public class CustomOperationRequestPart implements OperationRequestPart {
    private final String name;
    private final String submittedFileName;
    private final byte[] content;
    private final HttpHeaders headers;

    public CustomOperationRequestPart(String name, String submittedFileName, byte[] content,
                    HttpHeaders headers) {
        this.name = name;
        this.submittedFileName = submittedFileName;
        this.content = content;
        this.headers = headers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSubmittedFileName() {
        return submittedFileName;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public String getContentAsString() {
        if (this.content != null && this.content.length > 0) {
            return Base64.getEncoder().encodeToString(this.content);
        }
        return null;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
