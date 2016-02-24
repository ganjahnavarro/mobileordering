package com.mobile.mobileordering.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simplyph on 1/26/2016.
 */
public class InventoryManager implements Parcelable {

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

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
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

    private String category;
    private int menuid;
    private int inventoryID;
    private String name;
    private int qty;

    public InventoryManager(String category, int menuid, int id, String name, int qty) {
        this.category = category;
        this.menuid = menuid;
        this.inventoryID = id;
        this.name = name;
        this.qty = qty;
    }

    protected InventoryManager(Parcel in) {
        this.category = in.readString();
        this.menuid = in.readInt();
        this.inventoryID = in.readInt();
        this.name = in.readString();
        this.qty = in.readInt();
    }

    public static final Creator<InventoryManager> CREATOR = new Creator<InventoryManager>() {
        @Override
        public InventoryManager createFromParcel(Parcel in) {
            return new InventoryManager(in);
        }

        @Override
        public InventoryManager[] newArray(int size) {
            return new InventoryManager[size];
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
        dest.writeInt(inventoryID);
        dest.writeString(name);
        dest.writeInt(qty);
    }
}
