package com.example.quiz.vo;

import com.example.quiz.constants.ResMessage;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class DeleteReq {

    @NotEmpty(message = ResMessage.ConstantsMessage.PARAM_QUIZ_ID_LIST_ERROR)
    private List<Integer> quizIdList;

    public List<Integer> getQuizIdList() {
        return quizIdList;
    }
}
