package com.example.quiz.vo;

import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public class CreateReq extends Quiz {

    @Valid
    @NotEmpty(message = ResMessage.ConstantsMessage.PARAM_QUES_LIST_ERROR)
    private List<Question> questionList;

    public CreateReq(){

    }

    public CreateReq(int id, String name, String description, LocalDate startDate, LocalDate endDate,//
                     boolean published, List<Question> questionList) {
        super(id, name, description, startDate, endDate, published);
        this.questionList = questionList;
    }

    public CreateReq(List<Question> questionList) {
        this.questionList = questionList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
}
