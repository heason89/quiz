package com.example.quiz.vo;

public class OptionCountVo {
    private String option;

    private int count;

    public OptionCountVo() {
    }

    public OptionCountVo(String option, int count) {
        this.option = option;
        this.count = count;
    }

    public String getOption() {
        return option;
    }

    public int getCount() {
        return count;
    }
}
