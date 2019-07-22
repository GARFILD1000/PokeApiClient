package com.example.pokemonclient.utils;

import android.support.v7.util.DiffUtil;

import com.example.pokemonclient.models.Poke;

import java.util.List;

//class for DiffUtil that optimize changing data in recycler view
public class PokeDiffUtilCallback extends DiffUtil.Callback {
    private final List<Poke> oldList;
    private final List<Poke> newList;

    public PokeDiffUtilCallback(List<Poke> oldList, List<Poke> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Poke oldPoke = oldList.get(oldItemPosition);
        Poke newPoke = newList.get(newItemPosition);
        return oldPoke.getId().equals(newPoke.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Poke oldPoke = oldList.get(oldItemPosition);
        Poke newPoke = newList.get(newItemPosition);
        boolean isSame = true;
        isSame &= oldPoke.getName().equals(newPoke.getName());
        isSame &= oldPoke.getAttack().equals(newPoke.getAttack());
        isSame &= oldPoke.getDefense().equals(newPoke.getDefense());
        isSame &= oldPoke.getHp().equals(newPoke.getHp());
        return isSame;
    }
}
