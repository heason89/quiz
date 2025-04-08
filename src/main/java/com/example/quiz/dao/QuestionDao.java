package com.example.quiz.dao;

import com.example.quiz.entity.Question;
import com.example.quiz.entity.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId> {

    @Modifying
    @Transactional
    @Query(value = "insert into question(quiz_id, ques_id, name,type, is_necessary, options) " +
            " values (:quizId, :quesId, :quesName, :type, :necessary, :options)",nativeQuery = true)
    public void insertQues(//
                       @Param("quizId") int quizId,//
                       @Param("quesId") int quesId,//
                       @Param("quesName") String quesName,//
                       @Param("type") String type,//
                       @Param("necessary") boolean necessary,//
                       @Param("options") String options);

    @Query(value ="select * from question where quiz_id = ?1", nativeQuery =true)
    public List<Question> getByQuizId(int quizId);

    @Modifying
    @Transactional
    @Query(value ="delete from question where quiz_id = ?1", nativeQuery =true)
    public void deleteByQuizId(int id);

    @Modifying
    @Transactional
    @Query(value ="delete from question where quiz_id in (?1)", nativeQuery =true)
    public void delete(List<Integer> quizIdList);
}
