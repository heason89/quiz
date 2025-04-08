package com.example.quiz.constants;

public enum QuesType {

    SINGLE("Single"),//
    MULTI("Multi"),//
    TEXT("Text"),//
    ;
    private String type;

    QuesType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean checkType(String inputType){
        for(QuesType item:QuesType.values()){
            if(inputType.equalsIgnoreCase(item.getType())){
                return true;
            }
        }
        return false;
    }
}
