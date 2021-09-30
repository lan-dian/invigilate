package com.hfut.invigilate.domain.user.entity;

public enum UserRole {

    Teacher(0, "老师"),
    Admin(1, "教务");

    private final Integer code;
    private final String name;

    public static UserRole getByCode(Integer code){
        UserRole[] values = UserRole.values();
        for (UserRole value : values) {
            if(value.getCode()==code){
                return value;
            }
        }
        throw new IllegalArgumentException("用户角色码不合法");
    }

    UserRole(int code, String name) {
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
