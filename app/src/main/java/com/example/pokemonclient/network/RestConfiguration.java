package com.example.pokemonclient.network;

import com.example.pokemonclient.models.PokeListModel;
import com.example.pokemonclient.models.PokeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//API for Retrofit, based on PokeApi server interface
public interface RestConfiguration {

    //getting list of pokemons from (offset) to (offset+limit) IDs
    @GET("/api/v2/pokemon")
    Call<PokeListModel> getPokeList(@Query("limit") int limit, @Query("offset") int offset);

    //getting info about single pokemon by name
    @GET("/api/v2/pokemon/{name}")
    Call<PokeModel> getPokeByName(@Path("name") String name);
}