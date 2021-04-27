package com.example.pssmobile.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BureauListResponse {
    @SerializedName("Status")
    @Expose
    public Boolean status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Detail")
    @Expose
    public BureauListDetails detail;

    public class BureauListDetails {

        @SerializedName("code")
        @Expose
        public Integer code;
        @SerializedName("data")
        @Expose
        public ArrayList<BureauList> data = null;

    }

    public class BureauList {

        @SerializedName("ID")
        @Expose
        public String id;
        @SerializedName("Bureau")
        @Expose
        public String bureau;
        @SerializedName("Email")
        @Expose
        public String email;

    }
}
