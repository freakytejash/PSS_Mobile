package com.example.pssmobile.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddSiteRequest {
    @SerializedName("Allocated_job_type")
    @Expose
    public List<String> allocatedJobType = null;
    @SerializedName("Email")
    @Expose
    public String email;
    @SerializedName("SiteName")
    @Expose
    public String siteName;
    @SerializedName("Suburb")
    @Expose
    public String suburb;
    @SerializedName("keys")
    @Expose
    public String keys;
    @SerializedName("Documents_attached")
    @Expose
    public ArrayList<DocumentsAttached> documentsAttached = null;
    @SerializedName("Post_Code")
    @Expose
    public String postCode;
    @SerializedName("PhoneNo")
    @Expose
    public String phoneNo;
    @SerializedName("Added_User")
    @Expose
    public String addedUser;
    @SerializedName("Customers12")
    @Expose
    public String customers12;
    @SerializedName("LONG")
    @Expose
    public String _long;
    @SerializedName("Invoicing_Schedule")
    @Expose
    public String invoicingSchedule;
    @SerializedName("SiteID")
    @Expose
    public String siteID;
    @SerializedName("SiteAddress")
    @Expose
    public String siteAddress;
    @SerializedName("sitebriefing")
    @Expose
    public Sitebriefing sitebriefing = new Sitebriefing();
    @SerializedName("Active")
    @Expose
    public Boolean active;
    @SerializedName("Email_2")
    @Expose
    public String email2;
    @SerializedName("LAT")
    @Expose
    public String lat;

    public class Sitebriefing {

        @SerializedName("url")
        @Expose
        public String url = "";

    }

    public class DocumentsAttached {

        @SerializedName("Document_Name")
        @Expose
        public String documentName;
        @SerializedName("File_attachment")
        @Expose
        public String fileAttachment;
        @SerializedName("Document_Url")
        @Expose
        public String documentUrl;


    }
}


