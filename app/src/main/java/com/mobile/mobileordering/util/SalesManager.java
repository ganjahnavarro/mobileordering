package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simplyph on 1/23/2016.
 */
public class SalesManager implements Parcelable{

    private String category;
    private int id;
    private String name;
    private int qty;
    private int price;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public SalesManager(String category, int id){
        this.category = category;
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public SalesManager(Parcel in) {
        this.category = in.readString();
        this.id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(id);
    }

    public static final Creator<SalesManager> CREATOR = new Creator<SalesManager>() {
        @Override
        public SalesManager createFromParcel(Parcel in) {
            return new SalesManager(in);
        }

        @Override
        public SalesManager[] newArray(int size) {
            return new SalesManager[size];
        }
    };
}
