package com.example.pssmobile.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static final String BASE_URL = "https://creator.zoho.com";
    private static final String BASE_URL_WRITER ="http://103.255.190.131/PSSApp/";
    private static RequestInterface loadInterface=null;
    private static Retrofit retrofit = null;
    private static Retrofit retrofitWriter = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getWriter() {
        if (retrofitWriter == null) {
            retrofitWriter = new Retrofit.Builder()
                    .baseUrl(BASE_URL_WRITER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWriter;
    }

   /* public static RequestInterface loadInterface(){
        if (loadInterface ==null){
            loadInterface = ApiClient.getWriter().create(RequestInterface.class);
        }
        return loadInterface;
    }*/
}
