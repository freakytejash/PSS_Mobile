package com.example.pssmobile.retrofit;



import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {
   /* @FormUrlEncoded
    @POST("/api/accountsperthsecurityservices/json/mobileapp/form/Scan_Checkpoints/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap")
    Call<ResponseBody> insertData(@Field("Scan_Checkpoint1") String str, @Field("IMEI") String str2);
*/
    @FormUrlEncoded
    @POST("/api/accountsperthsecurityservices/json/mobileapp/form/collected_chains/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap")
    Call<ResponseBody> insertDataKey(@Field("Key_Chain_Code") String str, @Field("IMEI") String str2);

    @FormUrlEncoded
    @POST("/api/accountsperthsecurityservices/json/mobileapp/form/Scan_Checkpoints/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap")
    Call<ResponseBody> offlineInsert(@Field("Scan_Checkpoint1") String str, @Field("Sync_Time") String str2, @Field("IMEI") String str3);


    @FormUrlEncoded
    @POST("/api/accountsperthsecurityservices/json/mobileapp/form/Scan_Checkpoints/record/add?authtoken=26e6d588c42d569d93fcb35917e5ce7f&scope=creatorap")
    Call<ResponseBody> insertData(@Field("Scan_Checkpoint1") String str, @Field("IMEI") String str2, @Field("Checkpoint_Name") String job);

}
