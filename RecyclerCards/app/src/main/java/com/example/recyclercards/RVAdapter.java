package com.example.recyclercards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Seksu on 01/07/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<HeroViewHolder> {

    Hero[] heroes;

    RVAdapter (Hero[] heroes) {
        this.heroes = heroes;
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        HeroViewHolder hvh = new HeroViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(HeroViewHolder heroViewHolder, int i) {
        heroViewHolder.portrait.setImageResource(heroes[i].getPortrait());
        heroViewHolder.name.setText(heroes[i].getName());
        heroViewHolder.role.setText(heroes[i].getRole());
        heroViewHolder.difficulty_level.setText(heroes[i].getDifficulty_level());
    }

    @Override
    public int getItemCount() {
        return heroes.length;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}