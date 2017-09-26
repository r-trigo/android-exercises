package com.example.recyclercards;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Seksu on 01/07/2016.
 */
public class HeroViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    ImageView portrait;
    TextView name;
    TextView role;
    TextView difficulty_level;

    public HeroViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.cv);
        portrait = (ImageView)itemView.findViewById(R.id.portrait);
        name = (TextView)itemView.findViewById(R.id.name);
        role = (TextView)itemView.findViewById(R.id.role);
        difficulty_level = (TextView)itemView.findViewById(R.id.difficulty_level);
    }
}
