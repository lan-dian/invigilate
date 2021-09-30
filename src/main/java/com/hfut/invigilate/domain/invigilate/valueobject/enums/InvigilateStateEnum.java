package com.hfut.invigilate.domain.invigilate.valueobject.enums;


public enum InvigilateStateEnum {

    Init(0,"初始化");

    private final int code;
    private final String name;

    InvigilateStateEnum(int code, String name){
        this.code=code;
        this.name=name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
