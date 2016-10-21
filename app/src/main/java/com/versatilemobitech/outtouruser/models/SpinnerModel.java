package com.versatilemobitech.outtouruser.models;

public class SpinnerModel {
    private String title;

    public SpinnerModel(String str) {
        setTitle(str);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
