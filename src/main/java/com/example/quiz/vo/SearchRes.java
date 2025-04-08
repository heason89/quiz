package com.example.quiz.vo;

import com.example.quiz.entity.Quiz;

import java.util.List;

public class SearchRes extends BasicRes{
    private List<Quiz> quizList;

    public SearchRes() {
    }

    public SearchRes(int code, String message) {
        super(code, message);
    }

    public SearchRes(int code, String message, List<Quiz> quizList) {
        super(code, message);
        this.quizList = quizList;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }
}
