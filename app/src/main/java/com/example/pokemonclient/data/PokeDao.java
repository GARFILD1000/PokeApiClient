package com.example.pokemonclient.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pokemonclient.models.Poke;

import java.util.List;

//data access object interface for database
@Dao
public interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Poke poke);

    @Update
    void update(Poke poke);

    @Delete
    void delete(Poke poke);

    @Query("DELETE FROM pokes")
    void deleteAllPokes();

    @Query("SELECT * FROM pokes")
    LiveData<List<Poke>> getAllPokes();


}
