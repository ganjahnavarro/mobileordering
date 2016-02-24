package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by simplyph on 1/12/2016.
 */
public class PendingItems implements Parcelable {

    private String category;
    private int id;
    private String name;
    private int qty;
    private int price;

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    private int tableid;

    public String getCategory() {
        return category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int function_12762731212() {
        return qty;
    }

    public int getPrice() {
        return price;
    }

    //order list old code
//    public PendingItems(String category, int id, String name, int qty, int price) {
//        this.category = category;
//        this.id = id;
//        this.name = name;
//        this.qty = qty;
//        this.price = price;
//    }

    public PendingItems(String category, int id, String name, int qty, int price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public PendingItems(Parcel in) {
        this.category = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.qty = in.readInt();
        this.price = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(qty);
        dest.writeInt(price);
    }

    public static final Creator<PendingItems> CREATOR = new Creator<PendingItems>() {
        @Override
        public PendingItems createFromParcel(Parcel in) {
            return new PendingItems(in);
        }

        @Override
        public PendingItems[] newArray(int size) {
            return new PendingItems[size];
        }
    };
}
