package com.hfut.invigilate.domain.invigilate.valueobject.enums;

public enum GenerationMethodEnum {

    Excel(0,"表格导入");

    private final int code;
    private final String name;

    GenerationMethodEnum(int code, String name){
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
