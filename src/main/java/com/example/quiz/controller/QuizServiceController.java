package com.example.quiz.controller;

import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class QuizServiceController {

    @Autowired
    private QuizService quizService;

    @PostMapping(value = "quiz/create")
    public BasicRes create(@Valid @RequestBody CreateReq req) throws Exception {
        return quizService.create(req);
    }

    @GetMapping(value = "quiz/get_all")
    public SearchRes getAll() {
        return quizService.getAll();
    }

    @PostMapping(value = "quiz/get_all")
    public SearchRes getAll(@RequestBody SearchReq req) {
        return quizService.getAll(req);
    }

    // 呼叫此 API 的路徑: localhost:8080/quiz/get_by_ques_id?quizId=編號
    // ? 後面接著的是 key = value，key 是要 mapping 的變數名稱，value 是參數值，多個 key = value 用 & 串接
    @GetMapping(value = "quiz/get_by_quiz_id")
    public GetQuestionsRes qetQuestionsByQuizId(//
            @RequestParam(value = "quizId") int quizId) {
        return quizService.qetQuestionsByQuizId(quizId);
    }

    @PostMapping(value = "quiz/update")
    public BasicRes update(@Valid @RequestBody UpdateReq req){
        return quizService.update(req);
    }

    @PostMapping(value = "quiz/delete")
    public BasicRes delete(@Valid @RequestBody DeleteReq req){

        return quizService.delete(req);
    }



}

