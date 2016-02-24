package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simplyph on 1/25/2016.
 */
public class OrderManager implements Parcelable {

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
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

    private String category;
    private int menuid;
    private int id;
    private String name;
    private int qty;
    private int price;
    private int tableid;

    public OrderManager(String category, int menuid, int id, String name, int qty, int price){
        this.category = category;
        this.menuid = menuid;
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public OrderManager(Parcel in) {
        this.category = in.readString();
        this.menuid = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        this.qty = in.readInt();
        this.price = in.readInt();
    }

    public static final Creator<OrderManager> CREATOR = new Creator<OrderManager>() {
        @Override
        public OrderManager createFromParcel(Parcel in) {
            return new OrderManager(in);
        }

        @Override
        public OrderManager[] newArray(int size) {
            return new OrderManager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(menuid);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(qty);
        dest.writeInt(price);
    }

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }
}
