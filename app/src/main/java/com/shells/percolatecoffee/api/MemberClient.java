package com.shells.percolatecoffee.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.api.models.CoffeeListResource;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;

public class MemberClient {

    private static String API_URL;

    public static String getApiUrl() {
        return MemberClient.API_URL;
    }

    public interface PHQuizMemberClient {

        /* PH Endpoints */
        @GET("/coffee/")
        void getCoffeeAll(@Header("Authorization") String token, Callback<CoffeeListResource> cb);
    }

    public static PHQuizMemberClient getApiClient() {
        ObjectMapper mapper = new ObjectMapper();
        JacksonConverter converter = new JacksonConverter(mapper);

        RestAdapter.LogLevel log;
        MemberClient.API_URL = BuildConfig.API_URL;
        if (BuildConfig.DEBUG) {
            log = RestAdapter.LogLevel.FULL;
        } else {
            log = RestAdapter.LogLevel.BASIC;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(converter)
                .setEndpoint(MemberClient.API_URL)
                .setLogLevel(log)
                .build();

        return restAdapter.create(PHQuizMemberClient.class);
    }

}
