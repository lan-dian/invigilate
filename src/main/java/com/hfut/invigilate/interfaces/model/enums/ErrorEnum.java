package com.hfut.invigilate.interfaces.model.enums;

public enum ErrorEnum {

    UnLogin(-1,"未登陆");

    private final int code;
    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
