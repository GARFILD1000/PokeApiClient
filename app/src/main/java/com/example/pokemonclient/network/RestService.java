package com.example.pokemonclient.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//this singleton provides rest requests, that defined in rest configuration
public class RestService {

    public static String API_BASE_URL = "https://pokeapi.co/";
    private static Retrofit retrofit;
    private static RestService restService;

    public static RestService getInstance() {
        if (restService == null) {
            restService = new RestService();
        }
        return restService;
    }

    private RestService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RestConfiguration getJSONApi() {
        return retrofit.create(RestConfiguration.class);
    }

}
