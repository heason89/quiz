package com.example.quiz.entity;

import com.example.quiz.constants.ResMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Entity
@Table(name="quiz")
public class Quiz {

    @Id
    @Column(name ="id")
    private int id;

    @NotBlank(message = ResMessage.ConstantsMessage.PARAM_QUIZ_NAME_ERROR)
    @Column(name ="name")
    private String name;

    @NotBlank(message = ResMessage.ConstantsMessage.PARAM_DESCRIPTION_ERROR)
    @Length(max = 200,message = ResMessage.ConstantsMessage.PARAM_DESCRIPTION_LENGTH_TOO_LONG)
    @Column(name ="description")
    private String description;

    @NotNull(message = ResMessage.ConstantsMessage.PARAM_START_DATE_ERROR)
    @Column(name ="start_date")
    private LocalDate startDate;

    @NotNull(message = ResMessage.ConstantsMessage.PARAM_END_DATE_ERROR)
    @Column(name ="end_date")
    private LocalDate endDate;

    @Column(name ="is_published")
    private boolean published;

    public Quiz() {
    }

    public Quiz(int id, String name, String description, LocalDate startDate, LocalDate endDate, boolean published) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.published = published;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isPublished() {
        return published;
    }
}
