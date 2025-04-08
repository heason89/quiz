package com.example.quiz.vo;

import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;

import java.util.List;

public class GetQuestionsRes extends BasicRes{



    private List<Question> questionList;

    public GetQuestionsRes() {
    }

    public GetQuestionsRes(int code, String message) {
        super(code, message);
    }

    public GetQuestionsRes(int code, String message, List<Question> questionList) {
        super(code, message);
        this.questionList = questionList;
    }



    public List<Question> getQuestionList() {
        return questionList;
    }


}
