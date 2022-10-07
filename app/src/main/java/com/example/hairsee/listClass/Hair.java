package com.example.hairsee.listClass;


public class Hair {
    private String title;
    private int imgResource;

    public Hair(String title, int imgResource) {
        title = title;
        imgResource = imgResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

}