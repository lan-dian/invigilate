package com.hfut.invigilate.interfaces.model.enums;

public enum ExamStateEnum {

    FINISHED(0,"完成"),
    TODAY(1,"今天"),
    UNFINISHED(2,"待监考"),
    TO_BE_REPLACED(3,"等待调换"),
    TO_BE_CONFIRMED(4,"待确认");

    private final int code;
    private final String name;

    ExamStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
