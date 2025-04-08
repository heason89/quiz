package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedbackRes;
import com.example.quiz.vo.FillinReq;
import com.example.quiz.vo.StatisticsRes;

public interface FeedbackService {
    public BasicRes fillin(FillinReq req);

    public FeedbackRes feedback(int quizId);

    public StatisticsRes statistics(int quizId);
}
