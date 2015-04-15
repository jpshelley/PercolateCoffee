package com.shells.percolatecoffee.api;

import com.shells.percolatecoffee.BuildConfig;
import com.shells.percolatecoffee.api.models.CoffeeResource;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

public class MemberClient {

    private static String API_URL;

    public static String getApiUrl() {
        return MemberClient.API_URL;
    }

    public interface PHQuizMemberClient {

        /* Coffee Endpoints */
        @GET("/coffee/")
        void getCoffeeAll(@Header("Authorization") String token, Callback<ArrayList<CoffeeResource>> cb);

        @GET("/coffee/{coffeeId}/")
        void getCoffee(@Header("Authorization") String token, @Path("coffeeId") String coffeeId, Callback<CoffeeResource> cb);
    }

    public static PHQuizMemberClient getApiClient() {
        RestAdapter.LogLevel log;
        MemberClient.API_URL = BuildConfig.API_URL;
        if (BuildConfig.DEBUG) {
            log = RestAdapter.LogLevel.FULL;
        } else {
            log = RestAdapter.LogLevel.BASIC;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new retrofit.converter.JacksonConverter())
                .setEndpoint(MemberClient.API_URL)
                .setLogLevel(log)
                .build();

        return restAdapter.create(PHQuizMemberClient.class);
    }

}
