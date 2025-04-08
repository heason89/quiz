package com.example.quiz.vo;

import java.util.List;

public class QuesAnswerVo extends QuesIdAnswerVo{
    private String quesName;

    public QuesAnswerVo() {
    }

    public QuesAnswerVo(int quesId, List<String> answers, String quesName) {
        super(quesId, answers);
        this.quesName = quesName;
    }

    public String getQuesName() {
        return quesName;
    }

    public void setQuesName(String quesName) {
        this.quesName = quesName;
    }
}
