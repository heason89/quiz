package com.example.quiz.service.ifs;

import com.example.quiz.vo.*;

import java.time.LocalDate;

public interface QuizService {

    public BasicRes create(CreateReq req) throws Exception;

    public SearchRes getAll();

    public SearchRes getAll(SearchReq req);

    public GetQuestionsRes qetQuestionsByQuizId(int quizId);

    public BasicRes update(UpdateReq req);

    public BasicRes delete(DeleteReq req);

}
