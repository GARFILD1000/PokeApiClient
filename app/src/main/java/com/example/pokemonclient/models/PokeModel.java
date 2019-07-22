package com.example.pokemonclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//model for deserialization JSON to POJO
//store all received information about single pokemon
public class PokeModel {
    private Integer id;
    private String name;
    private Integer weight;
    private Integer height;
    private List<Types> types;
    private Sprites sprites;
    private List<Stats> stats;
    @SerializedName("base_experience")
    private Integer baseExperience;

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
        List<String> list = new LinkedList<>();
        for(Types i: types ){
            list.add(i.type.name);
        }
        return list;
    }


    public Integer getAttack() {
        return getValueFromStats("attack");
    }

    public Integer getDefence() {
        return getValueFromStats("defense");
    }

    public Integer getHp() {
        return getValueFromStats("hp");
    }

    public Integer getSpeed(){
        return getValueFromStats("speed");
    }

    public Integer getBaseExperience(){
        return baseExperience;
    }

    private Integer getValueFromStats(String key){
        Integer value = 0;
        for(Stats i: stats){
            if (i.stat.name.equals(key)){
                value = i.base_stat;
            }
        }
        return value;
    }

    public List<String> getAllImagesUrl(){
        List<String> allImagesUrl = new LinkedList<>();
        if(sprites.getFront_default() != null){
            allImagesUrl.add(sprites.getFront_default());
        }
        if(sprites.getFront_female() != null){
            allImagesUrl.add(sprites.getFront_female());
        }
        if(sprites.getFront_shiny() != null){
            allImagesUrl.add(sprites.getFront_shiny());
        }
        if(sprites.getFront_shiny_female() != null){
            allImagesUrl.add(sprites.getFront_shiny_female());
        }
        return allImagesUrl;
    }

    public class Sprites {
        private String front_default;
        private String front_female;
        private String front_shiny;
        private String front_shiny_female;

        public String getFront_default(){
            return this.front_default;
        }
        public String getFront_female(){
            return this.front_female;
        }
        public String getFront_shiny(){
            return this.front_shiny;
        }
        public String getFront_shiny_female(){
            return this.front_shiny_female;
        }
    }

    public class Type {
        private String name;
        private String url;

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
    }

    public class Types {
        private int slot;
        private Type type;

        public void setSlot(int slot){
            this.slot = slot;
        }
        public int getSlot(){
            return this.slot;
        }
        public void setType(Type type){
            this.type = type;
        }
        public Type getType(){
            return this.type;
        }
    }

    public class Stats {
        private int base_stat;
        private int effort;
        private Stat stat;

        public void setBase_stat(int base_stat){
            this.base_stat = base_stat;
        }
        public int getBase_stat(){
            return this.base_stat;
        }
        public void setEffort(int effort){
            this.effort = effort;
        }
        public int getEffort(){
            return this.effort;
        }
        public void setStat(Stat stat){
            this.stat = stat;
        }
        public Stat getStat(){
            return this.stat;
        }
    }

    public class Stat {
        private String name;
        private String url;

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setUrl(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
    }
}
