package com.example.pokemonclient.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pokemonclient.data.PokeDao;
import com.example.pokemonclient.data.PokeDatabase;
import com.example.pokemonclient.models.Poke;

import java.util.List;

//this class make work with database better
//and easier by completing some operations in non UI thread
public class PokeRepository {
    private PokeDao pokeDao;
    private LiveData<List<Poke>> allPokes;

    public PokeRepository(Application application){
        PokeDatabase database = PokeDatabase.getInstance(application);
        pokeDao = database.pokeDao();
        allPokes = pokeDao.getAllPokes();
    }

    public void insert(Poke poke){
        new InsertPokeAsyncTask(pokeDao).execute(poke);
    }

    public void update(Poke poke){
        new UpdatePokeAsyncTask(pokeDao).execute(poke);
    }

    public void delete(Poke poke){
        new DeletePokeAsyncTask(pokeDao).execute(poke);
    }

    public void deleteAllPokes(){
        new DeleteAllPokesAsyncTask(pokeDao).execute();
    }

    public LiveData<List<Poke>> getAllPokes(){
        return allPokes;
    }



    private static class InsertPokeAsyncTask extends AsyncTask<Poke, Void, Void>{
        private PokeDao pokeDao;

        private InsertPokeAsyncTask(PokeDao pokeDao){
            this.pokeDao = pokeDao;
        }

        @Override
        protected Void doInBackground(Poke... pokes) {
            pokeDao.insert(pokes[0]);
            return null;
        }
    }

    private static class DeleteAllPokesAsyncTask extends AsyncTask<Void, Void, Void>{
        private PokeDao pokeDao;

        private DeleteAllPokesAsyncTask(PokeDao pokeDao){
            this.pokeDao = pokeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pokeDao.deleteAllPokes();
            return null;
        }
    }

    private static class UpdatePokeAsyncTask extends AsyncTask<Poke, Void, Void>{
        private PokeDao pokeDao;

        private UpdatePokeAsyncTask(PokeDao pokeDao){
            this.pokeDao = pokeDao;
        }

        @Override
        protected Void doInBackground(Poke... pokes) {
            pokeDao.update(pokes[0]);
            return null;
        }
    }
    private static class DeletePokeAsyncTask extends AsyncTask<Poke, Void, Void>{
        private PokeDao pokeDao;

        private DeletePokeAsyncTask(PokeDao pokeDao){
            this.pokeDao = pokeDao;
        }

        @Override
        protected Void doInBackground(Poke... pokes) {
            pokeDao.delete(pokes[0]);
            return null;
        }
    }

}
