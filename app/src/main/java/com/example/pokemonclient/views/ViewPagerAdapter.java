package com.example.pokemonclient.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

//custom view pager for widget with sliding images
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;                //list of fragments with images
    Context context;

    public ViewPagerAdapter(FragmentManager fragmentManager, Context context){
        super(fragmentManager);
        this.context = context;
        fragmentList = new LinkedList<>();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public Fragment getFragment(Integer position){
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

}
