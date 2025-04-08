package com.example.quiz.vo;

import java.util.List;

public class StatisticsVo {
    private int quesId;

    private String quesName;

    private String type;

    private boolean necessary;

    private List<OptionCountVo> optionCountVoList;

    public StatisticsVo() {
    }

    public StatisticsVo(int quesId, String quesName, String type, boolean necessary) {
        this.quesId = quesId;
        this.quesName = quesName;
        this.type = type;
        this.necessary = necessary;
    }

    public StatisticsVo(int quesId, String quesName, String type, boolean necessary, List<OptionCountVo> optionCountVoList) {
        this.quesId = quesId;
        this.quesName = quesName;
        this.type = type;
        this.necessary = necessary;
        this.optionCountVoList = optionCountVoList;
    }

    public int getQuesId() {
        return quesId;
    }

    public void setQuesId(int quesId) {
        this.quesId = quesId;
    }

    public String getQuesName() {
        return quesName;
    }

    public void setQuesName(String quesName) {
        this.quesName = quesName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public void setNecessary(boolean necessary) {
        this.necessary = necessary;
    }

    public List<OptionCountVo> getOptionCountVoList() {
        return optionCountVoList;
    }

    public void setOptionCountVoList(List<OptionCountVo> optionCountVoList) {
        this.optionCountVoList = optionCountVoList;
    }
}
