package com.example.pssmobile.data.model;

public class Getters {
    private String Address;
    private String Bureau;
    private String Checkpoint_Name;
    private String Checkpoint_description;
    private String Checkpoint_ID;
    private String Checkpoint_picture = "https://i.imgur.com/tGbaZCY.jpg";
    private String ID;


    public String getCheckpoint_Name() {
        return this.Checkpoint_Name;
    }

    public String getID() {
        return this.ID;
    }

    public String getBureau() {
        return this.Bureau;
    }

    public String getAddress() {
        return this.Address;
    }

    public String getCheckpoint_description() {
        return this.Checkpoint_description;
    }

    public String getImage() {
        return this.Checkpoint_picture;
    }

    public String getCheckpoint_ID() {
        return Checkpoint_ID;
    }

    public void setCheckpoint_ID(String checkpoint_ID) {
        Checkpoint_ID = checkpoint_ID;
    }
}
