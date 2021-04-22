package com.example.pssmobile.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientWriter {
    private static final String BASE_URL_WRITER ="http://103.255.190.131/PSSApp/";
    private static RequestWriterInterface loadInterface=null;
    private static Retrofit retrofitWriter = null;


    public static Retrofit getWriter() {
        if (retrofitWriter == null) {
            retrofitWriter = new Retrofit.Builder()
                    .baseUrl(BASE_URL_WRITER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWriter;
    }

    public static RequestWriterInterface loadInterfaceWriter(){
        if (loadInterface ==null){
            loadInterface = ApiClientWriter.getWriter().create(RequestWriterInterface.class);
        }
        return loadInterface;
    }
}
