package com.example.pssmobile.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddSiteRequest implements Serializable {
    @SerializedName("customers12")
    @Expose
    public String customers12;
    @SerializedName("siteName")
    @Expose
    public String siteName;
    @SerializedName("siteAddress")
    @Expose
    public String siteAddress;
    @SerializedName("suburb")
    @Expose
    public String suburb;
    @SerializedName("post_Code")
    @Expose
    public String postCode;

    @SerializedName("contact_person")
    @Expose
    public String contactPerson;
    @SerializedName("phoneNo")
    @Expose
    public String phoneNo;
    @SerializedName("email_2")
    @Expose
    public String email2;
    @SerializedName("allocated_job_type")
    @Expose
    public List<String> allocatedJobType = null;

    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("invoicing_Schedule")
    @Expose
    public String invoicingSchedule;
    @SerializedName("sitebriefing")
    @Expose
    public Sitebriefing sitebriefing;
    @SerializedName("keys")
    @Expose
    public String keys;
    @SerializedName("key_Number")
    @Expose
    public String keyNumber;
    @SerializedName("siteref")
    @Expose
    public String siteref;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("operations_note")
    @Expose
    public String operationsNote;
    @SerializedName("clamp_info")
    @Expose
    public String clampInfo;
    @SerializedName("ccheck")
    @Expose
    public Boolean ccheck;
    @SerializedName("documents_attached")
    @Expose
    public ArrayList<DocumentsAttached> documentsAttached = null;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("long")
    @Expose
    public String _long;
    @SerializedName("alarm_Response_Rate")
    @Expose
    public String alarmResponseRate;
    @SerializedName("mobile_Patrol_Rate")
    @Expose
    public String mobilePatrolRate;
    @SerializedName("additional_time_after_30_min_on_site")
    @Expose
    public String additionalTimeAfter30MinOnSite;

    public static class Sitebriefing {

        @SerializedName("url")
        @Expose
        private String url;

        public Sitebriefing(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class DocumentsAttached {

        @SerializedName("Document_Name")
        @Expose
        public String documentName;
        @SerializedName("File_attachment")
        @Expose
        public String fileAttachment;
        @SerializedName("Document_Url")
        @Expose
        public String documentUrl;
        @SerializedName("FileName")
        @Expose
        public String fileName;

        public DocumentsAttached(String documentName, String fileAttachment, String documentUrl, String fileName) {
            this.documentName = documentName;
            this.fileAttachment = fileAttachment;
            this.documentUrl = documentUrl;
            this.fileName = fileName;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
        }

        public String getFileAttachment() {
            return fileAttachment;
        }

        public void setFileAttachment(String fileAttachment) {
            this.fileAttachment = fileAttachment;
        }

        public String getDocumentUrl() {
            return documentUrl;
        }

        public void setDocumentUrl(String documentUrl) {
            this.documentUrl = documentUrl;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}


