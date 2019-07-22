package com.example.pokemonclient.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

//class that store data about single pokemon
@Entity(tableName = "pokes")
public class Poke implements Serializable {

    @PrimaryKey
    private  Integer id;
    private String name;
    private Integer weight;
    private Integer height;
    //@Ignore
    @TypeConverters({PokeConverter.class})
    private List<String> types;
    private Integer attack;
    private Integer defense;
    private Integer hp;
    private Integer speed;
    //@Ignore

    @TypeConverters({PokeConverter.class})
    private List<String> allImagesUrl;
    //private List<String> abilities;
    private Integer baseExperience;

    public Poke(){
        allImagesUrl = new LinkedList<>();
        types = new LinkedList<>();
    }

    public String getTypesString(){
        String typesString = "";
        for(String i: types){
            typesString = typesString.concat(" ").concat(i);
        }
        return typesString;
    }

    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getHeight() {
        return height;
    }


    public List<String> getTypes() {
        return types;
    }

    public List<String> getAllImagesUrl() {
        return allImagesUrl;
    }

    public Integer getBaseExperience(){
        return baseExperience;
    }


    public void setBaseExperience(Integer baseExperience){
        this.baseExperience = baseExperience;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getAttack() {
        return attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getHp() {
        return hp;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public void setAllImagesUrl(List<String> allImages) {
        this.allImagesUrl = allImages;
    }

    public static class PokeConverter{
        @TypeConverter
        public String fromTypes(List<String> types){
            String typesString = "";
            for(String x: types){
                typesString = typesString.concat(x).concat(" ");
            }
            return typesString;
        }

        @TypeConverter
        public List<String> toTypes(String typesString) {
            List<String> types = new LinkedList<>();
            String[] typesArray = typesString.split(" ");
            for (String x : typesArray) {
                types.add(x);
            }
            return types;
        }
    }
}

