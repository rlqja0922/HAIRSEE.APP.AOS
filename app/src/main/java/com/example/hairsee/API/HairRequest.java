package com.example.hairsee.API;

import java.io.File;

public class HairRequest {
    boolean status;
    String fcm,hairType,hairColor;
    File img;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
