package com.example.pokemonclient.network;

import android.support.annotation.NonNull;

import com.example.pokemonclient.models.PokeListModel;
import com.example.pokemonclient.models.PokeModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//rest controller takes responsibility for sending requests to server and catching responses from it
public class RestController {

    private OnResponseListener onResponseListener;

    public RestController(OnResponseListener onResponseListener){
        this.onResponseListener = onResponseListener;
    }

    //custom listener interface, that sending signal about requests and responses
    public interface OnResponseListener {
        void onListReceived(PokeListModel pokeListModel);
        void onListFailed();
        void onPokeReceived(PokeModel pokeModel);
        void onPokeFailed();
    }

    public void getPokeList(Integer limit, Integer offset){
        RestService.getInstance()
                .getJSONApi()
                .getPokeList(limit,offset)
                .enqueue(new Callback<PokeListModel>() {
            @Override
            public void onResponse(@NonNull Call<PokeListModel> call, @NonNull Response<PokeListModel> response) {
                PokeListModel list = response.body();
                if (list != null) {
                    onResponseListener.onListReceived(list);
                }
                else{
                    onResponseListener.onListFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokeListModel> call, @NonNull Throwable t) {
                onResponseListener.onListFailed();
                t.printStackTrace();
            }
        });
    }

    public void getPokeByName(String name){
        RestService.getInstance()
                .getJSONApi()
                .getPokeByName(name)
                .enqueue(new Callback<PokeModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PokeModel> call, @NonNull Response<PokeModel> response) {
                        PokeModel poke = response.body();
                        if (poke != null) {
                            onResponseListener.onPokeReceived(poke);
                        }
                        else{
                            onResponseListener.onPokeFailed();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<PokeModel> call, @NonNull Throwable t) {
                        onResponseListener.onPokeFailed();
                        t.printStackTrace();
                    }
                });
    }

}
