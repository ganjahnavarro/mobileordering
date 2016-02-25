package com.mobile.mobileordering.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<Sales> parseFeedSales(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);

            ArrayList<Sales> salesManagerArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                /// Sizzling pata 2 190 Php 12121
                /// Sizzling pata 3 190 Php 12121

                if (!salesManagerArrayList.isEmpty()) {
                    boolean gotcha = false;

                    for (Sales sales : salesManagerArrayList) {
                        if (sales.getCategory().equalsIgnoreCase(jsonObject.getString("category")) && String.valueOf(sales.getId()).equalsIgnoreCase(String.valueOf(jsonObject.getInt("menuid")))) {
                            // Get
                            int current = sales.getQty();
                            int addedQty = jsonObject.getInt("qty");

                            // Set
                            sales.setQty(current + addedQty);
                            gotcha = true;
                            break;
                        }
                    }

                    if(!gotcha){
                        Sales newSales = new Sales(jsonObject.getString("category"), jsonObject.getInt("menuid"));

                        newSales.setName(jsonObject.getString("name"));
                        newSales.setQty(jsonObject.getInt("qty"));
                        newSales.setPrice(jsonObject.getInt("price"));

                        salesManagerArrayList.add(newSales);
                    }
                } else {

                    Sales newSales = new Sales(jsonObject.getString("category"), jsonObject.getInt("menuid"));

                    newSales.setName(jsonObject.getString("name"));
                    newSales.setQty(jsonObject.getInt("qty"));
                    newSales.setPrice(jsonObject.getInt("price"));

                    salesManagerArrayList.add(newSales);

                }
            }

            return salesManagerArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Order> parseFeedOrder(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);

            ArrayList<Order> orders = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = new Order(jsonObject.getString("category"), jsonObject.getInt("menuid"), jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getInt("qty"), jsonObject.getInt("price"));
                order.setTableid(jsonObject.getInt("tableid"));
                orders.add(order);
            }

            return orders;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<InventoryManager> parseFeedInventory(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);

            ArrayList<InventoryManager> inventoryManagerArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                InventoryManager inventoryManager = new InventoryManager(jsonObject.getString("category"), jsonObject.getInt("menuid"), jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getInt("inventory"));
                inventoryManagerArrayList.add(inventoryManager);
            }
            return inventoryManagerArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
