package com.bime.nocturne.server;

import retrofit.RestAdapter;

/**
 * Created by andy on 03/05/15.
 */
public class NocturneRestAdapter {
    public static NocturneRestApi getRestAdapter(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();
        NocturneRestApi service = restAdapter.create(NocturneRestApi.class);
        return service;
    }
}
