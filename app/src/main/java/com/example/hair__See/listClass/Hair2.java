package com.example.hair__See.listClass;

public class Hair2 {
    private String title;
    private int imgResource;
    private int type;
    private boolean pick;

    public boolean isPick() {
        return pick;
    }

    public void setPick(boolean pick) {
        this.pick = pick;
    }

    public Hair2(String title, int imgResource, int type , boolean pick) {
        this.title = title;
        this.imgResource = imgResource;
        this.type = type;
        this.pick = pick;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
