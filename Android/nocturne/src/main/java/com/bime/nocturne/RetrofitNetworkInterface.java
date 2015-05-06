package com.bime.nocturne;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by jbi on 18/03/2015.
 */
public class RetrofitNetworkInterface {

    private static RestAdapter restAdaptor = null;
    private static RetrofitNetworkService retrofitNetService = null;

    public static RetrofitNetworkService getService(Context ctx) {
        if (retrofitNetService == null) {
            retrofitNetService = getAdaptor(ctx).create(RetrofitNetworkService.class);
        }
        return retrofitNetService;
    }

    private static RestAdapter getAdaptor(Context ctx) {
        if (restAdaptor == null) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
                    .create();

            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
            final String serverAddr = "http://"
                    + settings
                    .getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT)
                    + ":"
                    + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT)
                    + "/";

            restAdaptor = new RestAdapter.Builder()
                    .setEndpoint(serverAddr)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }
        return restAdaptor;
    }

}
