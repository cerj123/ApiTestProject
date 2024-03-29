package com.model;

public class UserTimeResponse extends UserTime {
    private String updatedAt;

    public UserTimeResponse(){
        super();
    }

    public UserTimeResponse(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
