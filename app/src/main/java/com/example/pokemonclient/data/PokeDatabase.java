package com.example.pokemonclient.data;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.pokemonclient.models.Poke;

//database that use PokeDao API to store data
@Database(entities = {Poke.class}, version = 1)
public abstract class PokeDatabase extends RoomDatabase {
    private static PokeDatabase instance;

    public static final String POKE_DATABASE_NAME = "poke_database";

    public abstract PokeDao pokeDao();

    public static synchronized PokeDatabase getInstance(Context context){
        if (instance == null){
           instance = Room.databaseBuilder(context.getApplicationContext(), PokeDatabase.class, POKE_DATABASE_NAME)
                   .fallbackToDestructiveMigration()
                   .addCallback(roomCallback)
                   .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            //there we can do some work after creation of database
            super.onCreate(db);
        }

    };

}
