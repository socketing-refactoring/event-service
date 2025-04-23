package com.jeein.event.docs.util;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsUtil {

    public static RestDocumentationResultHandler doc(String identifier, Snippet... snippets) {
        return document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        snippets);
    }

    public static RestDocumentationResultHandler multipart_doc(String identifier, Snippet... snippets) {
        return document(identifier,
            preprocessRequest(
                new BinaryDataRemoverPreProcessor(), prettyPrint()
            )
            , preprocessResponse(prettyPrint()),
            snippets);
    }

}
