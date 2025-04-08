package com.example.quiz.service.impl;

import com.example.quiz.constants.QuesType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.dao.FeedbackDao;
import com.example.quiz.dao.QuestionDao;
import com.example.quiz.dao.QuizDao;
import com.example.quiz.entity.Feedback;
import com.example.quiz.entity.Question;
import com.example.quiz.service.ifs.FeedbackService;
import com.example.quiz.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private ObjectMapper mapper = new ObjectMapper();


    @Autowired
    private FeedbackDao feedbackDao;

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;

    @Override
    public BasicRes fillin(FillinReq req) {
        int quizId = req.getQuizId();
        //1. 檢查quiz
        // 是否已發布以及當前時間是否介於開始時間和結束時間內
        int count = quizDao.selectCountById(quizId, LocalDate.now());
        if(count != 1){
            return new BasicRes(ResMessage.OUT_OF_FILLIN_DATE_RANGE.getCode(),  //
                    ResMessage.OUT_OF_FILLIN_DATE_RANGE.getMessage());
        }
        //2. 檢查回饋資料
        //同一個email 只能填寫同意張問卷一次
        count =feedbackDao.selectCount(quizId,req.getEmail());
        if(count != 0){//表示同一個email沒有填寫過同一張問卷
            return new BasicRes(ResMessage.EMAIL_DUPLICATED.getCode(),  //
                    ResMessage.EMAIL_DUPLICATED.getMessage());
        }
        //3. 檢查答案
        List<QuesIdAnswerVo> quesIdAnswerList = req.getQuesIdAnswerList();
        //3.1 從DB撈資問題
        List<Question> questionList = questionDao.getByQuizId(quizId);
        //3.2 比對問題選項跟答案
        for (Question quesItem : questionList){
            int quesId = quesItem.getQuesId();
            String type = quesItem.getType();
            boolean necessary = quesItem.isNecessary();
            List<String> optionsList= new ArrayList<>();
            try {
                //只轉換 type 非 text 的，因為text沒有選項可以轉換
                if (!type.equalsIgnoreCase(QuesType.TEXT.getType())){
                    optionsList = mapper.readValue(quesItem.getOptions(), new TypeReference<>() {
                    });
                }
            }catch (Exception e){
                return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(),  //
                        ResMessage.PARAM_OPTIONS_ERROR.getMessage());
            }

            for (QuesIdAnswerVo voItem : quesIdAnswerList){
                List<String> answerList =new ArrayList<>();
                if(quesId == voItem.getQuesId()){
                    answerList = voItem.getAnswers();
                    // 經過上面的if判斷式，answerList可能會有答案，也可能沒有答案(因為沒有填)
                    // 排除1:必填但沒有答案
                    if (necessary && answerList.isEmpty()){// necessary 等同於 necessary == true
                        return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(),  //
                                ResMessage.ANSWER_REQUIRED.getMessage());
                    }
                    // 跳過type是text的，因為沒有選項可以比對
                    if (type.equalsIgnoreCase(QuesType.TEXT.getType())){
                        continue;
                    }
                    // 單選時答案不能有多個
                    if (type.equalsIgnoreCase(QuesType.SINGLE.getType())&&answerList.size()>1){
                        return new BasicRes(ResMessage.ONE_OPTION_IS_ALLOWED.getCode(),  //
                                ResMessage.ONE_OPTION_IS_ALLOWED.getMessage());
                    }
                    // 比對同一題的答案是否都是在選項中
                    boolean checkRes = checkAnswer(optionsList,answerList);
                    if(!checkRes){
                        return new BasicRes(ResMessage.ANSWER_OPTION_MISMATCH.getCode(),  //
                                ResMessage.ANSWER_OPTION_MISMATCH.getMessage());
                    }
                }

            }
        }
        // 存資料
        for (QuesIdAnswerVo voItem : quesIdAnswerList){
            // 將 List<String> answerList 轉換成 String
            try {
                String answerStr = mapper.writeValueAsString(voItem.getAnswers());
                feedbackDao.insert(quizId,voItem.getQuesId(),req.getName(),req.getPhone(),
                        req.getEmail(),req.getAge(),answerStr);
            }catch (JsonProcessingException e){
                return new BasicRes(ResMessage.ANSWER_PARSE_MISMATCH.getCode(),  //
                        ResMessage.ANSWER_PARSE_MISMATCH.getMessage());
            }catch (Exception e){
                throw e;
            }
        }
        return new BasicRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage());
    }

    @Override
    public FeedbackRes feedback(int quizId) {
        // 檢查參數
        if (quizId <=0){
            return new FeedbackRes(ResMessage.SUCCESS.getCode(),  //
                    ResMessage.SUCCESS.getMessage());
        }
        // 撈資料
        List<FeedbackDto> res = feedbackDao.selectFeedback(quizId);
        // 整理資料：透過相同的 email 把問題和回答整理在一起
        // Map<String,List<QuesAnswerVo>> 中的 key 是放 email
        Map<String,List<QuesAnswerVo>> map =new HashMap<>();
        String quizName ="";
        String description ="";
        Map<String,List<FeedbackVo>> emailFeedbackMap = new HashMap<>();
        for (FeedbackDto dto: res) {
            quizName = dto.getQuizName();
            description = dto.getDescription();
            String email = dto.getEmail();
            List<QuesAnswerVo> voList = new ArrayList<>();
            if (map.containsKey(email)) {
                //若 map 中已存在相同的 key(email)，就從 map 中把對應的 value 取出
                voList = map.get(email);
            }
            QuesAnswerVo vo = new QuesAnswerVo();
            vo.setQuesId(dto.getQuesId());
            vo.setQuesName(dto.getQuesName());
            try {
                //將 DB 中的 answer String 轉成 List<String>
                List<String> answerList = mapper.readValue(dto.getAnswer(), new TypeReference<>() {
                });
                vo.setAnswers(answerList);
            } catch (Exception e) {
                return new FeedbackRes(ResMessage.ANSWER_PARSE_ERROR.getCode(),  //
                        ResMessage.ANSWER_PARSE_ERROR.getMessage());
            }
            voList.add(vo);
            map.put(email, voList);
        }

        List<FeedbackVo> feedbackList =new ArrayList<>();
        a: for (FeedbackDto dto: res){
            String email = dto.getEmail();
            for (FeedbackVo vo : feedbackList){
                if(email.equalsIgnoreCase(vo.getEmail())){
                    continue a;
                }
            }
            FeedbackVo feedbackVo =new FeedbackVo();
            feedbackVo.setUserName(dto.getUserName());
            feedbackVo.setPhone(dto.getPhone());
            feedbackVo.setEmail(email);
            feedbackVo.setAge(dto.getAge());
            feedbackVo.setFillinDate(dto.getFillinDate());
            feedbackVo.setQuesAnswerList(map.get(dto.getEmail()));
            feedbackList.add(feedbackVo);
        }
        return new FeedbackRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage(),quizName,description,feedbackList);
    }

    @Override
    public StatisticsRes statistics(int quizId) {
        // 從 question 找選項: 不直接從答案找主要預防可能會有某個選項都沒有人選
        List<Question> questionList = questionDao.getByQuizId(quizId);
        List<StatisticsVo> statisticsVoList = new ArrayList<>();
        // Map<問題編號,多個選項>
        Map<Integer,List<String>> quesIdOptionsMap = new HashMap<>();
        for(Question item : questionList){
            // 將DB中的question 相關資訊設定到 StatisticsVo 中，先排除統計
            StatisticsVo vo = new StatisticsVo(item.getQuesId(),item.getName()
                    ,item.getType(),item.isNecessary());
            statisticsVoList.add(vo);
            // 找出每一題非text的選項
            if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())){
                // 將選項字串轉換成 List<String>
                try {
                    List<String> optionList = mapper.readValue(item.getOptions(), new TypeReference<>() {
                    });
                    quesIdOptionsMap.put(item.getQuesId(),optionList);
                }catch (Exception e){
                    return new StatisticsRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(),  //
                            ResMessage.OPTIONS_PARSE_ERROR.getMessage());
                }
            }else {
                quesIdOptionsMap.put(item.getQuesId(),null);
            }


        }
        Map<Integer,List<String>> quesIdAnswerMap =getAnswers(quizId);
        if(quesIdAnswerMap == null){
            return new StatisticsRes(ResMessage.ANSWER_PARSE_ERROR.getCode(),  //
                    ResMessage.ANSWER_PARSE_ERROR.getMessage());
        }
        //計算選項的回答次數
        Map<Integer,List<OptionCountVo>> map = computeAnswerCount(quesIdOptionsMap,quesIdAnswerMap);
        for (StatisticsVo vo: statisticsVoList) {
            int quesId =vo.getQuesId();
            vo.setOptionCountVoList(map.get(quesId));
        }
        return new StatisticsRes(ResMessage.SUCCESS.getCode(),  //
                ResMessage.SUCCESS.getMessage(),statisticsVoList);
    }
    private Map<Integer,List<OptionCountVo>> computeAnswerCount(Map<Integer,List<String>> quesIdOptionsMap,//
                             Map<Integer,List<String>> quesIdAnswerMap){
        //Map<問題編號,List<OptionCountVo>>
        Map<Integer,List<OptionCountVo>> map =new HashMap<>();
        for(Map.Entry<Integer,List<String>> mapItem : quesIdOptionsMap.entrySet()){
            List<OptionCountVo> optionCountVoList = new ArrayList<>();
            int quesId = mapItem.getKey();
            List<String> optionsList = mapItem.getValue();
            //答案包含了: 1.單選的選項 2.多選的選項 3.簡答內容
            if(quesIdAnswerMap.containsKey(quesId) && optionsList != null){
                List<String> answersList =quesIdAnswerMap.get(quesId);
                int size =answersList.size();
                for (String option:optionsList ){
                    answersList.removeAll(List.of(option));
                    int optionSize = size -answersList.size();
                    OptionCountVo vo =new OptionCountVo(option,optionSize);
                    //改變 answersList size
                    size =size - optionSize;
                }
            }
            map.put(quesId,optionCountVoList);
        }
        return map;
    }
    private Map<Integer,List<String>> getAnswers(int quizId){
        //取答案
        List<Feedback> feedbackList =feedbackDao.selectByQuizId(quizId);
        // Map<問題編號,多個答案>
        Map<Integer,List<String>> quesIdAnswerMap = new HashMap<>();
        for (Feedback item : feedbackList){
            if(!StringUtils.hasText(item.getAnswer())){
                int quesId =item.getQuesId();
                // 將答案字串轉換成 List<String>
                try {
                    List<String> answerList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
                    });
                    if(quesIdAnswerMap.containsKey(quesId)){
                        //取出已存在map中的 answer List
                        List<String> list =quesIdAnswerMap.get(quesId);
                        //把兩個List相加
                        list.addAll(answerList);
                        //把結果放回map中
                        quesIdAnswerMap.put(quesId,list);
                    }else{
                        quesIdAnswerMap.put(quesId,answerList);
                    }
                }catch (Exception e){
                    return null;
                }
            }
        }
        return quesIdAnswerMap;
    }
    private boolean checkAnswer(List<String> optionsList,List<String> answerList){
        //切串接的字串
        List<String> newOptionsList = new ArrayList<>();
        for(String item : optionsList){
            String[] strArray =item.split(":");
            newOptionsList.addAll(List.of(strArray));
        }
        //比對選項和答案
        for (String answer : answerList){
            // 假設一種情況， optionsList = ["A", "B", "C", "D"], answerList = ["A", "a"]
            // 若下方的 if 是沒有驚嘆號的判斷式， if(optionsList.contains(answer))，且回傳 true
            // 當 answerList 中第一個答案 A 判斷後就會回傳 true，後面的的 a 就會沒判斷到
            if (!optionsList.contains(answer)){
                return false;
            }
        }
        return true;
    }
}
