package com.example.pssmobile.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteCheckpointResponse {
    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public Detail detail;

    public class Detail {

        @SerializedName("code")
        @Expose
        public Integer code;
        @SerializedName("data")
        @Expose
        public List<SiteCheckpoint> siteCheckpoint = null;

    }

    public class SiteCheckpoint {
        @SerializedName("ID")
        @Expose
        public String id;
        @SerializedName("Job_Name")
        @Expose
        public String jobName;
        @SerializedName("PSS_Job_ID")
        @Expose
        public String pSSJobID;
    }
}
