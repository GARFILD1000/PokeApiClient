package com.example.pokemonclient.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonclient.R;
import com.example.pokemonclient.models.Poke;
import com.example.pokemonclient.utils.PokeDiffUtilCallback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

//class that provides data for recycle view
public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.PokeViewHolder> implements Serializable {

    private List<Poke> pokesList = new LinkedList<>();
    private OnPokeClickListener onPokeClickListener;
    private Context context;
    //sorting flags
    public Boolean sortByAttack = false;
    public Boolean sortByDefense = false;
    public Boolean sortByHp = false;

    //comparator, that used for comparing in sorting
    class IncPokeComparator implements Comparator<Poke>{
        @Override
        public int compare(Poke pokeOne, Poke pokeTwo){
            int result = 0;

                Integer sumOfOne = 0;
                Integer sumOfTwo = 0;

                sumOfOne += (sortByAttack) ? pokeOne.getAttack() : 0;
                sumOfTwo += (sortByAttack) ? pokeTwo.getAttack() : 0;

                sumOfOne += (sortByDefense) ? pokeOne.getDefense() : 0;
                sumOfTwo += (sortByDefense) ? pokeTwo.getDefense() : 0;

                sumOfOne += (sortByHp) ? pokeOne.getHp() : 0;
                sumOfTwo += (sortByHp) ? pokeTwo.getHp() : 0;

                result = (sumOfOne > sumOfTwo) ? 1 : (sumOfOne < sumOfTwo) ? -1 : 0;

            return result;
        }
    }
    //another comparator, but used for decreasing sorting
    class DecPokeComparator implements Comparator<Poke>{
        @Override
        public int compare(Poke pokeOne, Poke pokeTwo){
            return -(new IncPokeComparator().compare(pokeOne, pokeTwo));
        }
    }

    public PokeAdapter(Context context) {
        this.context = context;
    }

    public void sortItems(){
        Collections.sort(pokesList, new DecPokeComparator());
        notifyDataSetChanged();
    }

    public void setItems(List<Poke> newPokes){
        Collections.sort(newPokes, new DecPokeComparator());
        PokeDiffUtilCallback pokeDiffUtilCallback =
                new PokeDiffUtilCallback(pokesList, newPokes);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(pokeDiffUtilCallback);
        this.pokesList = newPokes;
        diffResult.dispatchUpdatesTo(this);
    }

    public void addItem(Poke newPoke){
        this.pokesList.add(newPoke);
        Collections.sort(pokesList, new DecPokeComparator());
        notifyItemInserted(pokesList.indexOf(newPoke));
    }

    public void addItems(Collection<Poke> newPokes){
        List<Poke> newPokesList = new LinkedList<>(pokesList);
        newPokesList.addAll(newPokes);
        Collections.sort(newPokesList, new DecPokeComparator());

        PokeDiffUtilCallback pokeDiffUtilCallback =
                new PokeDiffUtilCallback(pokesList, newPokesList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(pokeDiffUtilCallback);

        this.pokesList = newPokesList;
        diffResult.dispatchUpdatesTo(this);
    }

    public void deleteItems() {
        pokesList.clear();
        notifyItemRangeRemoved(0, pokesList.size());
    }

    public List<Poke> getItems(){
        return this.pokesList;
    }

    public interface OnPokeClickListener {
        void onPokeClick(Poke poke);
    }

    public void setOnPokeClickListener(OnPokeClickListener onPokeClickListener ){
        this.onPokeClickListener = onPokeClickListener;
    }

    //there are the moment, when element creating and we can create our view
    @NonNull
    @Override
    public PokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poke_item_view, parent, false);
        return new PokeViewHolder(view);
    }

    //there we can fill view with our data for single element
    @Override
    public void onBindViewHolder(@NonNull PokeViewHolder pokeViewHolder, int position) {
        pokeViewHolder.bind(pokesList.get(position));
        if ((position == 0)&&(sortByAttack || sortByDefense || sortByHp)){
            pokeViewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPokemonRed));
        }
        else{
            pokeViewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return pokesList.size();
    }

    //custom holder, that helps fill data in created for element view
    class PokeViewHolder extends RecyclerView.ViewHolder{
        private ImageView pokeImageView;
        private TextView pokeNameTextView;
        private TextView pokeHeightTextView;
        private TextView pokeWeightTextView;
        private TextView pokeTypeTextView;
        private TextView pokeAttackTextView;
        private TextView pokeDefenceTextView;
        private TextView pokeHPTextView;

        public PokeViewHolder(View itemView){
            super(itemView);
            pokeImageView = itemView.findViewById(R.id.pokeImageView);
            pokeNameTextView = itemView.findViewById(R.id.pokeNameTextView);
            pokeTypeTextView = itemView.findViewById(R.id.pokeTypeTextView);
            pokeAttackTextView = itemView.findViewById(R.id.pokeAttackTextView);
            pokeDefenceTextView = itemView.findViewById(R.id.pokeDefenceTextView);
            pokeHPTextView = itemView.findViewById(R.id.pokeHPTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Poke poke = pokesList.get(getLayoutPosition());
                    onPokeClickListener.onPokeClick(poke);
                }
            });

        }

        //bind values from fields to views
        public void bind(Poke poke) {
            pokeNameTextView.setText(poke.getName().toUpperCase());
            pokeTypeTextView.setText(poke.getTypesString());
            pokeAttackTextView.setText(String.valueOf(poke.getAttack()));
            pokeDefenceTextView.setText(String.valueOf(poke.getDefense()));
            pokeHPTextView.setText(String.valueOf(poke.getHp()));
            if (!poke.getAllImagesUrl().isEmpty() && !poke.getAllImagesUrl().get(0).isEmpty()) {
                String pokeImageUrl = poke.getAllImagesUrl().get(0);
                Picasso.get()
                        .load(pokeImageUrl)
                        .into(pokeImageView);
            }
            //pokeImageView.setVisibility((!poke.getAllImagesUrl().isEmpty()) ? View.VISIBLE : View.INVISIBLE);
        }
    }



}
