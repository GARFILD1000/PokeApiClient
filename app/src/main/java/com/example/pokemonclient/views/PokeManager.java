package com.example.pokemonclient.views;

import android.os.Handler;

import com.example.pokemonclient.models.Poke;
import com.example.pokemonclient.models.PokeListModel;
import com.example.pokemonclient.models.PokeModel;
import com.example.pokemonclient.network.RestController;

import java.util.LinkedList;
import java.util.List;

//class that controls work with REST interface
public class PokeManager {
    private Integer pokesNumber = 0;                    //count of all pokemons on the server
    private Integer pokesLoadedNumber = 0;              //count of pokemons that been loaded
    private Integer startLoadingPosition = 0;           //position, from what starts loading pokemons from server
    private Integer pageSize = 30;                      //minimal size of page with pokemons
    public static Boolean isPokesLoading = false;       //flag that shows, is pokemons loading from server now
    private Integer pokesRequestedNumber = 0;           //number of requests without response
    private Boolean timeoutHandlerWait = false;         //flag that shows, is handler's schedule started or not
    private Integer waitingResponseTime = 30000;        //time of waiting for full page of pokemons
    private Handler timeoutHandler;
    private PokeAdapter pokeAdapter;
    private PokesViewModel pokesViewModel;
    private RestController restController;

    public PokeManager(PokesViewModel pokesViewModel, PokeAdapter pokeAdapter){
        this.pokesViewModel = pokesViewModel;
        this.pokeAdapter = pokeAdapter;
        initRestController();
    }
    private OnLoadingListener onLoadingListener;

    //custom listener interface, that sending signal about state of loading date
    public interface OnLoadingListener {
        void onLoadingStarted();
        void onLoadingFinished();
        void onLoadingFailed();
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener){
        this.onLoadingListener = onLoadingListener;
    }

    public Integer getStartLoadingPosition() {
        return startLoadingPosition;
    }

    public void setStartLoadingPosition(Integer startLoadingPosition) {
        this.startLoadingPosition = startLoadingPosition;
    }


    private void loadPokesFromServer(Integer limit, Integer offset){
        if (!timeoutHandlerWait) {
            timeoutHandler = new Handler();
            Runnable makeToast = new Runnable(){
                @Override
                public void run(){
                    if(isPokesLoading) {
                        isPokesLoading = false;
                        onLoadingListener.onLoadingFailed();
                    }
                    timeoutHandlerWait = false;
                }
            };
            //start handler's schedule
            timeoutHandler.postDelayed(makeToast, waitingResponseTime);
            timeoutHandlerWait = true;
        }
        pokesRequestedNumber = 0;
        pokesLoadedNumber = 0;
        restController.getPokeList(limit, offset);
        isPokesLoading = true;
        onLoadingListener.onLoadingStarted();
    }

    //function that loads more pokemons to list from database or server
    public void loadMorePokes(){
        //if there is no loading process and we can load more pokes to list

        if(!isPokesLoading && (pokesNumber > pokeAdapter.getItemCount() || pokesNumber == 0)) {
            Integer offset = startLoadingPosition + pokeAdapter.getItemCount();
            offset = (pokesNumber != 0) ? offset % pokesNumber : offset;
            loadPokesFromServer(pageSize, offset);

        }
    }

    //init rest controller and all listeners
    private void initRestController(){
        RestController.OnResponseListener onResponseListener = new RestController.OnResponseListener(){
            //temporary list for requested pokemon's names
            private List<String> requestedPokes = new LinkedList<>();
            //temporary list for received pokemons
            private List<Poke> receivedPokes = new LinkedList<>();

            //receiving list of pokemons
            @Override
            public void onListReceived(PokeListModel pokeListModel) {
                pokesNumber = pokeListModel.getCount();
                if (pokeAdapter.getItemCount() < pokesNumber) {
                    for (PokeListModel.Results poke : pokeListModel.getResults()) {
                        restController.getPokeByName(poke.getName());
                        requestedPokes.add(poke.getName());
                        pokesRequestedNumber++;
                    }
                }
            }

            //receiving data about single pokemon
            @Override
            public void onPokeReceived(PokeModel pokeModel){
                Poke poke = new Poke();
                poke.setId(pokeModel.getId());
                poke.setHp(pokeModel.getHp());
                poke.setName(pokeModel.getName());
                poke.setAttack(pokeModel.getAttack());
                poke.setDefense(pokeModel.getDefence());
                poke.setSpeed(pokeModel.getSpeed());
                poke.setHeight(pokeModel.getHeight());
                poke.setBaseExperience(pokeModel.getBaseExperience());
                poke.setAllImagesUrl(pokeModel.getAllImagesUrl());
                poke.setWeight(pokeModel.getWeight());
                poke.setTypes(pokeModel.getTypes());
                receivedPokes.add(poke);
                pokesViewModel.insert(poke);
                requestedPokes.remove(poke.getName());

                pokesLoadedNumber++;
                pokesRequestedNumber--;

                if(isPokesLoading && pokesRequestedNumber.equals(0)){
                    if (requestedPokes.isEmpty()){
                        isPokesLoading = false;
                        //Log.d("Received","pokes loading done");
                        if(pokesLoadedNumber < pageSize){
                            loadMorePokes();
                        }
                        else{
                            finishLoading();
                        }
                    }
                    else{
                        loadBadPokes();
                    }
                }
            }
            //receiving list error
            @Override
            public void onListFailed(){
                loadMorePokes();
            }

            //receiving pokemon error
            @Override
            public void onPokeFailed(){
                pokesRequestedNumber--;
                if(isPokesLoading && pokesRequestedNumber.equals(0)){
                    loadBadPokes();
                }
            }

            //function for loading pokemons, that wasn't received but they are in the list
            public void loadBadPokes(){
                for(String badPoke : requestedPokes){
                    restController.getPokeByName(badPoke);
                    pokesRequestedNumber++;
                }
            }

            public void finishLoading(){
                //pokeAdapter.addItems(receivedPokes);
                receivedPokes.clear();
                onLoadingListener.onLoadingFinished();
                timeoutHandler.removeCallbacksAndMessages(null);
                timeoutHandlerWait = false;
            }
        };
        restController = new RestController(onResponseListener);
    }
}
