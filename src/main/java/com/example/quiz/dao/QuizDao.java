package com.example.quiz.dao;

import com.example.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

    //SQL語法中冒號 :後面的name 對應的變數名稱是 @Param 括號中的字串 name，其餘皆同
    @Modifying
    @Transactional
    @Query(value = "insert into quiz(name, description, start_Date, end_Date, is_published) " +
            " values (:name, :description, :startDate, :endDate, :published)",nativeQuery = true)
    public void insertQuiz(//
                       @Param("name") String name,//
                       @Param("description")String description,//
                       @Param("startDate")LocalDate startDate,//
                       @Param("endDate")LocalDate endDate,//
                       @Param("published")boolean published);

    @Query(value = "select max(id) from quiz",nativeQuery = true)
    public int selectMaxQuizId();

    @Query(value ="select * from quiz where id = ?1", nativeQuery =true)
    public List<Quiz> getByQuizId(int quizId);

    @Query(value ="select * from quiz", nativeQuery =true)
    public List<Quiz> getAll();

    @Query(value ="select * from quiz where name like %?1% and start_date >= ?2 " +
            " and end_date <= ?3", nativeQuery =true)
    public List<Quiz> getAll(String quizName, LocalDate startDate, LocalDate endDate);

    @Query(value ="select count(id) from quiz where id = ?1", nativeQuery =true)
    public int selectCountById(int id);

    @Query(value ="delete from question where quiz_id = ?1", nativeQuery =true)
    public void deleteById(int id);

    @Modifying
    @Transactional
    @Query(value ="update quiz set name = :name, description = :description," +
            " start_date = :startDate, end_date = :endDate, is_published = :published" +
            " where id = :id", nativeQuery =true)
    public void updateById(
            @Param("id") int id,
            @Param("name") String name,//
            @Param("description")String description,//
            @Param("startDate")LocalDate startDate,//
            @Param("endDate")LocalDate endDate,//
            @Param("published")boolean published);

    @Modifying
    @Transactional
    @Query(value ="delete from quiz where id in (?1)", nativeQuery =true)
    public void delete(List<Integer> quizIdList);

    @Query(value ="select count(id) from quiz where id = ?1 and ?2 >= start_date " +
            " and ?2 <= end_date and is_published = true", nativeQuery =true)
    public int selectCountById(int id, LocalDate now);
}
