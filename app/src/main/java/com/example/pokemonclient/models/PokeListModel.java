package com.example.pokemonclient.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//model for deserialization JSON to POJO
//store information about count of pokemons and list of requested pokemon's names
public class PokeListModel {
    private int count;
    private String next;
    private String previous;
    @SerializedName("results")
    private List<Results> pokeList;

    public void setCount(int count){
        this.count = count;
    }
    public int getCount(){
        return this.count;
    }
    public void setNext(String next){
        this.next = next;
    }
    public String getNext(){
        return this.next;
    }
    public void setPrevious(String previous){
        this.previous = previous;
    }
    public String getPrevious(){
        return this.previous;
    }
    public void setResults(List<Results> results){
        this.pokeList = results;
    }
    public List<Results> getResults(){
        return this.pokeList;
    }

    public class Results
    {
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