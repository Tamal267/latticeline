package com.example.latticeline;

public class assignMent {
    String gpname, text, code, input, assignId, users;

    public assignMent(String gpname, String text, String code, String input, String assignId, String users) {
        this.gpname = gpname;
        this.text = text;
        this.code = code;
        this.input = input;
        this.assignId = assignId;
        this.users = users;
    }

    public String getGpname() {
        return gpname;
    }

    public void setGpname(String gpname) {
        this.gpname = gpname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getAssignId() {
        return assignId;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }
}
