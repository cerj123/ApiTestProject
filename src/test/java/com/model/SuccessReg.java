package com.model;

public class SuccessReg {
    private Integer id;
    private String token;

    public SuccessReg() {
        super();
    }

    public SuccessReg(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
