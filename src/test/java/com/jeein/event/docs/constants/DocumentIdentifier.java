package com.jeein.event.docs.constants;

public class DocumentIdentifier {
    public static final String EVENT = "event";
    public static final String EVENT_CREATION = EVENT + "/creation";
    public static final String EVENT_CREATION_SUCCESS = EVENT_CREATION + "/success";
    public static final String EVENT_RETRIEVAL = EVENT + "/retrieval";
    public static final String EVENT_LIST_RETRIEVAL = EVENT_RETRIEVAL + "/list";
    public static final String EVENT_LIST_RETRIEVAL_SUCCESS = EVENT_LIST_RETRIEVAL + "/success";

    public static final String SINGLE_EVENT_RETRIEVAL = EVENT_RETRIEVAL + "/single";
    public static final String SINGLE_EVENT_RETRIEVAL_SUCCESS = SINGLE_EVENT_RETRIEVAL + "/success";


    public static final String EVENT_DELETION = EVENT + "/deletion";

    public static final String EVENT_DELETION_SUCCESS = EVENT_DELETION + "/success";
}
