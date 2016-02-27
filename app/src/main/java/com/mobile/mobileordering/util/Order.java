package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {

    private int id;
    private int tableid;
    private int batchid;
    private String category;
    private String name;
    private int menuid;
    private int qty;
    private int price;
    private int status;

    public Order(int id, int tableid, int batchid, String category, String name, int menuid, int qty, int price, int status){
        this.id = id;
        this.tableid = tableid;
        this.batchid = batchid;
        this.category = category;
        this.menuid = menuid;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.status = status;
    }

    public Order(Parcel in) {
        this.id = in.readInt();
        this.tableid = in.readInt();
        this.batchid = in.readInt();
        this.category = in.readString();
        this.name = in.readString();
        this.menuid = in.readInt();
        this.qty = in.readInt();
        this.price = in.readInt();
        this.status = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tableid);
        dest.writeInt(batchid);
        dest.writeString(category);

        dest.writeString(name);
        dest.writeInt(menuid);
        dest.writeInt(qty);
        dest.writeInt(price);
        dest.writeInt(status);
    }

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

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBatchid() {
        return batchid;
    }

    public void setBatchid(int batchid) {
        this.batchid = batchid;
    }
}
