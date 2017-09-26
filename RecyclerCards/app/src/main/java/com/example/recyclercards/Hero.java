package com.example.recyclercards;

import android.os.Parcel;
import android.os.Parcelable;

public class Hero implements Parcelable {

    private int portrait;
    private String name;
    private String role;
    private String difficulty_level;

    public Hero() {
    };

    public Hero(int portrait, String name, String role, String difficulty_level) {
        this.portrait = portrait;
        this.name = name;
        this.role = role;
        this.difficulty_level = difficulty_level;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDifficulty_level() {
        return difficulty_level;
    }

    public void setDifficulty_level(String difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    protected Hero(Parcel in) {
        portrait = in.readInt();
        name = in.readString();
        role = in.readString();
        difficulty_level = in.readString();
    }

    public static final Creator<Hero> CREATOR = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int size) {
            return new Hero[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(portrait);
        parcel.writeString(name);
        parcel.writeString(role);
        parcel.writeString(difficulty_level);
    }
}
