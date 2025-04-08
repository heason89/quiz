package com.example.quiz.service.impl;

import com.example.quiz.constants.QuesType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.dao.QuestionDao;
import com.example.quiz.dao.QuizDao;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;

    //rollbackFor = Exception.class
    //@Transactional 只有錯誤發生在RuntimeException(或其子類別)時才會將資料回朔
    //因為 Exception 是所有XXXException 的父類別，只要是發生 XXXException 時，資料回朔都會有效
    // rollbackFor : import 的 library 是 org.springframework.transaction.annotation.Transactional;
    // rollbackon : jakarta.transaction.Transactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BasicRes create(CreateReq req) throws Exception {
        // 參數檢查: req 的參數已透過 @Valid 做基本檢查
        // 開始時間不能比結束時間晚
        if (req.getStartDate().isAfter(req.getEndDate())) {
            return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(),//
                    ResMessage.PARAM_DATE_ERROR.getMessage());
        }
        //檢查問題內容
        BasicRes checkRes = checkQuestion(req.getQuestionList());
        if (checkRes != null) {
            return checkRes;
        }
        try {
            //將資料寫進DB
            //將資料寫進quiz
            quizDao.insertQuiz(req.getName(), req.getDescription(), req.getStartDate(),//
                    req.getEndDate(), req.isPublished());
            //取的最新一筆資料的quiz_id
            int quizId = quizDao.selectMaxQuizId();
            //將資料寫進question
            for (Question item : req.getQuestionList()) {
                questionDao.insertQues(quizId, item.getQuesId(), item.getName(), item.getType()
                        , item.isNecessary(), item.getOptions());
            }
        } catch (Exception e) {
            //錯誤要拋(throw)出去，@Transactional 才會生效(將資料回朔)
            throw e;
        }

        return new BasicRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage());
    }

    @Override
    public SearchRes getAll() {
        List<Quiz> list = quizDao.getAll();
        return new SearchRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage(), list);
    }

    @Override
    public SearchRes getAll(SearchReq req) {
        // 轉換值
        String quizName = req.getQuizName();
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();
        // 將quizName = null 或是 quizName = 全空白字串時，轉換成空字串
        if (!StringUtils.hasText(quizName)) {
            quizName = "";
        }
        // startDate 沒有帶值表示不是條件之一，但因為 SQL 的語法，可以把 startDate 替換成很早的時間
        if (startDate==null) {
            //將quizName = null 或是 quizName = 全空白字串
            startDate = LocalDate.of(1970,1,1);
        }
        // endDate 沒有帶值表示不是條件之一，但因為 SQL 的語法，可以把 endDate 替換成很久之後的時間
        if (endDate==null) {
            //將quizName = null 或是 quizName = 全空白字串
            endDate = LocalDate.of(9999,12,31);
        }
        List<Quiz> list =quizDao.getAll(quizName,startDate,endDate);
        return new SearchRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage(), list);
    }

    @Override
    public GetQuestionsRes qetQuestionsByQuizId(int quizId) {
        if (quizId <= 0) {
            return new GetQuestionsRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(),  //
                    ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
        }
        List<Question> list = questionDao.getByQuizId(quizId);
        return new GetQuestionsRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage(), list);
    }

    //因為update的方法包含了跨表操作，所以要將這些Dao的操作當成同一次的行為
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BasicRes update(UpdateReq req) {
        // 要檢查 quiz 的 id 是否存在，因為最後是先將問題的資料刪除後再新增，而不是用更新語法
        // 不使用更新語法的考量原因是，更新前有5題，更新後變成4題，若是用更新語法，原本的第5題還會存在
        // 因 quiz 的 id 不存在，刪除問題時資料不會變更，但接著新增時就會資料新增，且這些資料沒有對應的 quiz_id
        int quizId = req.getId();
        int count = quizDao.selectCountById(quizId);
        if (count != 1) {
            return new BasicRes(ResMessage.QUIZ_ID_NOT_FOUND.getCode(),//
                    ResMessage.QUIZ_ID_NOT_FOUND.getMessage());
        }
        // 開始時間不能比結束時間晚
        if (req.getStartDate().isAfter(req.getEndDate())) {
            return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(),//
                    ResMessage.PARAM_DATE_ERROR.getMessage());
        }
        //檢查問題內容
        BasicRes checkRes = checkQuestion(req.getQuestionList());
        if (checkRes != null) {
            return checkRes;
        }

        //檢查 Question 中 的 quizId 是否與 Quiz 的 id 值一樣
        for (Question item : req.getQuestionList()) {
            {
                if (item.getQuizId() != quizId) {
                    return new BasicRes(ResMessage.QUIZ_ID_MISMATCH.getCode(),//
                            ResMessage.QUIZ_ID_MISMATCH.getMessage());
                }
            }
        }
        try {
            //1.更新問卷
            quizDao.updateById(quizId, req.getName(), req.getDescription()//
                    , req.getStartDate(), req.getEndDate(), req.isPublished());
            //2.刪除同張問卷的所有問題
            questionDao.deleteByQuizId(quizId);
            //3.新增問題
            //將資料寫進question
            for (Question item : req.getQuestionList()) {
                questionDao.insertQues(quizId, item.getQuesId(), item.getName(), item.getType()
                        , item.isNecessary(), item.getOptions());
            }
        } catch (Exception e) {
            throw e;
        }
        return new BasicRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BasicRes delete(DeleteReq req) {
        try {
            quizDao.delete(req.getQuizIdList());
            questionDao.delete(req.getQuizIdList());
        }catch (Exception e){
            throw e;
        }
        return new BasicRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage());
    }

    private BasicRes checkQuestion(List<Question> list) {
        for (Question item : list) {
            // 檢查 question type
            if (!QuesType.checkType(item.getType())) {
                return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
                        ResMessage.QUES_TYPE_ERROR.getMessage());
            }
            // 排除: type 不為 text(單選或複選)時，選項沒有值
            if (!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
                    && !StringUtils.hasText(item.getOptions())) {
                return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(),//
                        ResMessage.PARAM_OPTIONS_ERROR.getMessage());

            }
            // 排除: type 為 text 時，選項有值
            if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
                    && StringUtils.hasText(item.getOptions())) {
                return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(),//
                        ResMessage.PARAM_OPTIONS_ERROR.getMessage());
            }
            //問卷的 type=text時，options沒資料，所以不用執行後續的選項個數檢查
            if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
                return null;
            }
            // 檢查單選或複選題，選項至少要有兩個
            // 將選項字串轉成 List<String>: 要先確定當初創建問卷時，前端的多個選項是陣列，且使用 Stringify 轉成字串型態
            // 前端選項原本格式(陣列): ["1:aa","2:bb", "3:cc"]或["aa","bb", "cc"]
            ObjectMapper mapper = new ObjectMapper();
            try {
                List<String> optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
                });
                if (item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())
                        || item.getType().equalsIgnoreCase(QuesType.MULTI.getType())) {
                    if (optionsList.size() < 2) {
                        return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(),//
                                ResMessage.OPTIONS_SIZE_ERROR.getMessage());
                    }
                }
            } catch (Exception e) {
                return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(),//
                        ResMessage.OPTIONS_PARSE_ERROR.getMessage());
            }
        }
        return null;
    }
}
