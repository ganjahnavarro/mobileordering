package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Feedback implements Parcelable{

    private int id;
    private int rating;
    private String message;
    private String name;

    public Feedback(int id, int rating, String message, String name){
        this.id = id;
        this.rating = rating;
        this.message = message;
        this.name = name;
    }

    public Feedback(Parcel in) {
        this.id = in.readInt();
        this.rating = in.readInt();
        this.message = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(rating);
        dest.writeString(message);
        dest.writeString(name);
    }

    public int getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

}
