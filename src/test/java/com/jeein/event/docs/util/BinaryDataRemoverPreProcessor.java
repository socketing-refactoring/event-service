package com.jeein.event.docs.util;

import java.util.List;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public class BinaryDataRemoverPreProcessor implements OperationPreprocessor {
    @Override
    public OperationRequest preprocess(OperationRequest request) {
        List<OperationRequestPart> updatedParts = request.getParts().stream().map(part -> {
            if (part.getName().equals("thumbnail")) {
                return new CustomOperationRequestPart(part.getName(), part.getSubmittedFileName(),
                                new byte[0], part.getHeaders());
            }
            return part;
        }).toList();

        return new OperationRequestFactory().create(request.getUri(), request.getMethod(),
                        request.getContent(), request.getHeaders(), updatedParts, request.getCookies());
    }

    @Override
    public OperationResponse preprocess(OperationResponse operationResponse) {
        return null;
    }
}
