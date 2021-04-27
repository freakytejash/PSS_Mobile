package com.example.pssmobile.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCheckpointRequest {
    @SerializedName("select_Job")
    @Expose
    public String selectJob;
    @SerializedName("checkpoint_ID")
    @Expose
    public String checkpointID;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("checkpoint_Name")
    @Expose
    public String checkpointName;
    @SerializedName("checkpoint_description")
    @Expose
    public String checkpointDescription;
    @SerializedName("checkpoint_Entry1")
    @Expose
    public String checkpointEntry1;
    @SerializedName("mobile_input")
    @Expose
    public String mobileInput;
    @SerializedName("checkpoint_Picture")
    @Expose
    public String checkpointPicture;
    @SerializedName("how_many_scan")
    @Expose
    public Integer howManyScan;
}
