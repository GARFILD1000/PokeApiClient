package com.example.pokemonclient.views;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pokemonclient.data.PokeRepository;
import com.example.pokemonclient.models.Poke;

import java.util.List;

public class PokesViewModel extends AndroidViewModel {
    private PokeRepository repository;
    private LiveData<List<Poke>> allPokes;
    private PokeAdapter pokeAdapter;

    public void savePokeAdapter(PokeAdapter pokeAdapter){
        this.pokeAdapter = pokeAdapter;
    }

    public PokeAdapter restorePokeAdapter(){
        return pokeAdapter;
    }

    public PokesViewModel(@NonNull Application application){
        super(application);
        repository = new PokeRepository(application);
        allPokes = repository.getAllPokes();

    }

    public void insert(Poke poke){
        repository.insert(poke);
    }

    public void update(Poke poke){
        repository.update(poke);
    }

    public void delete(Poke poke){
        repository.delete(poke);
    }

    public void deleteAll(){
        repository.deleteAllPokes();
    }

    public LiveData<List<Poke>> getAllPokes(){
        return allPokes;
    }

}
