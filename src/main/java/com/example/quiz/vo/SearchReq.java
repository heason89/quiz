package com.example.quiz.vo;

import java.time.LocalDate;

public class SearchReq {

    private String quizName;

    private LocalDate startDate;

    private LocalDate endDate;

    public SearchReq() {
    }

    public SearchReq(String quizName, LocalDate startDate, LocalDate endDate) {
        this.quizName = quizName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getQuizName() {
        return quizName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
