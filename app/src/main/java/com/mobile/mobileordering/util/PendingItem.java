package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

public class PendingItem implements Parcelable {

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

    public int getQty() {
        return qty;
    }

    public int getPrice() {
        return price;
    }

    public PendingItem(String category, int id, String name, int qty, int price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public PendingItem(Parcel in) {
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

    public static final Creator<PendingItem> CREATOR = new Creator<PendingItem>() {
        @Override
        public PendingItem createFromParcel(Parcel in) {
            return new PendingItem(in);
        }

        @Override
        public PendingItem[] newArray(int size) {
            return new PendingItem[size];
        }
    };

}
