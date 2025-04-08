package com.example.quiz.entity;

import com.example.quiz.constants.ResMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="question")
@IdClass(value=QuestionId.class)
public class Question {

    @Min(value = 1,message = ResMessage.ConstantsMessage.PARAM_QUES_ID_ERROR)
    @Id
    @Column(name ="ques_id")
    private int quesId;

    @Id
    @Column(name ="quiz_id")
    private int quizId;

    @NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUES_NAME_ERROR)
    @Column(name ="name")
    private String name;

    @NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUES_TYPE_ERROR)
    @Column(name ="type")
    private String type;

    @Column(name ="is_necessary")
    private boolean necessary;

    //不用檢查，
    @Column(name ="options")
    private String options;

    public Question() {
    }

    public Question(int quizId,int quesId, String name, String type, boolean necessary, String options) {
        this.quesId = quesId;
        this.quizId = quizId;
        this.name = name;
        this.type = type;
        this.necessary = necessary;
        this.options = options;
    }

    public int getQuesId() {
        return quesId;
    }

    public int getQuizId() {
        return quizId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public String getOptions() {
        return options;
    }
}
