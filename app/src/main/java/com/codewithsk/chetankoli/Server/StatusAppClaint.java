package com.codewithsk.chetankoli.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatusAppClaint {
    public static String BASE_URL = "https://best-free-status.com/status-app/";
    public static Retrofit retrofit;
    private static StatusAppClaint claint;

    public StatusAppClaint()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized StatusAppClaint getStatusAppClaint() {
        if (claint == null)
        {
            claint = new StatusAppClaint();
        }
        return claint;
    }
    public Api getApi()
    {
        return retrofit.create(Api.class);
    }

}
