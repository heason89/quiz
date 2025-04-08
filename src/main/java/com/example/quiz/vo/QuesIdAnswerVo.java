package com.example.quiz.vo;

import java.util.List;

public class QuesIdAnswerVo {
    //參數不用驗證，因為有可能這一題是簡答題且是非必填，加上驗證反而會出錯

    private int quesId;

    private List<String> answers;

    public QuesIdAnswerVo() {
    }

    public QuesIdAnswerVo(int quesId, List<String> answers) {
        this.quesId = quesId;
        this.answers = answers;
    }

    public int getQuesId() {
        return quesId;
    }

    public void setQuesId(int quesId) {
        this.quesId = quesId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
