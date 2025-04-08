package com.example.quiz.vo;

import java.util.List;

public class FeedbackRes extends BasicRes{

    private String quizName;

    private String description;

    private List<FeedbackVo> quesAnswerList;

    public FeedbackRes() {
    }

    public FeedbackRes(int code, String message) {
        super(code, message);
    }

    public FeedbackRes(int code, String message, String quizName,
                       String description, List<FeedbackVo> quesAnswerList) {
        super(code, message);
        this.quizName = quizName;
        this.description = description;
        this.quesAnswerList = quesAnswerList;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getDescription() {
        return description;
    }

    public List<FeedbackVo> getQuesAnswerList() {
        return quesAnswerList;
    }
}
