package com.example.quiz.constants;

public enum ResMessage {

    SUCCESS(200,"Success!"),//
    PARAM_DATE_ERROR(400,"Param date error!"),//
    QUES_TYPE_ERROR(400,"Question type error!"),//
    PARAM_OPTIONS_ERROR(400,"Param options error!"),//
    OPTIONS_PARSE_ERROR(400,"Options parse error!"),//
    OPTIONS_SIZE_ERROR(400,"Options size error!"),//
    PARAM_QUIZ_ID_ERROR(400,"Param quiz_id error!"),//
    QUIZ_ID_MISMATCH(400,"Quiz_id mismatch!"),//
    QUIZ_ID_NOT_FOUND(404,"Quiz_id not found!"),//
    OUT_OF_FILLIN_DATE_RANGE(400, "Out of fillin date range!!"),//
    EMAIL_DUPLICATED(400, "Email duplicated!!"),//
    ANSWER_REQUIRED(400, "Answer required!!"),//
    ANSWER_OPTION_MISMATCH(400, "Answer option mismatch!!"),//
    ANSWER_PARSE_MISMATCH(400, "Answer parse mismatch!!"),//
    ANSWER_PARSE_ERROR(400, "Answer parse error!!"),//
    ONE_OPTION_IS_ALLOWED(400, "One option is allowed!!")
    ;
    private int code;

    private String message;

    ResMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ConstantsMessage{
        public static final String PARAM_QUIZ_NAME_ERROR = "Param quiz name error!";
        public static final String PARAM_DESCRIPTION_ERROR = "Param description error!";
        public static final String PARAM_DESCRIPTION_LENGTH_TOO_LONG = "Param description length too length!";
        public static final String PARAM_START_DATE_ERROR = "Param start_date error!";
        public static final String PARAM_END_DATE_ERROR = "Param end_date error!";
        public static final String PARAM_QUIZ_ID_ERROR = "Param quiz_id error!";
        public static final String PARAM_QUES_ID_ERROR = "Param ques_id error!";
        public static final String PARAM_QUES_NAME_ERROR = "Param question name error!";
        public static final String PARAM_QUES_TYPE_ERROR = "Param question type error!";
        public static final String PARAM_QUES_LIST_ERROR = "Param question list error!";
        public static final String PARAM_QUIZ_ID_LIST_ERROR = "Param quiz_id list error!";
        public static final String EMAIL_IS_NECESSARY = "Email is necessary!";

    }

}
