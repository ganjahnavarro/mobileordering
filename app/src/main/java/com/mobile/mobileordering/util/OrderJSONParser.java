package com.mobile.mobileordering.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by simplyph on 1/14/2016.
 */
public class OrderJSONParser {

    public static ArrayList<PendingItems> parseFeed(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);

            ArrayList<PendingItems> pendingItemsArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // String category, int id, String name, int qty, int price
                PendingItems pendingItems = new PendingItems(jsonObject.getString("category"), jsonObject.getInt("menuid"), jsonObject.getString("name"), jsonObject.getInt("qty"), jsonObject.getInt("price"));
                pendingItems.setTableid(jsonObject.getInt("tableid"));

                pendingItemsArrayList.add(pendingItems);
            }

            return pendingItemsArrayList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<SalesManager> parseFeedSales(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);

            ArrayList<SalesManager> salesManagerArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                /// Sizzling pata 2 190 Php 12121
                /// Sizzling pata 3 190 Php 12121

                if (!salesManagerArrayList.isEmpty()) {
                    boolean gotcha = false;

                    for (SalesManager salesManager : salesManagerArrayList) {
                        if (salesManager.getCategory().equalsIgnoreCase(jsonObject.getString("category")) && String.valueOf(salesManager.getId()).equalsIgnoreCase(String.valueOf(jsonObject.getInt("menuid")))) {
                            // Get
                            int current = salesManager.getQty();
                            int addedQty = jsonObject.getInt("qty");

                            // Set
                            salesManager.setQty(current + addedQty);
                            gotcha = true;
                            break;
                        }
                    }

                    if(!gotcha){
                        SalesManager newSales = new SalesManager(jsonObject.getString("category"), jsonObject.getInt("menuid"));

                        newSales.setName(jsonObject.getString("name"));
                        newSales.setQty(jsonObject.getInt("qty"));
                        newSales.setPrice(jsonObject.getInt("price"));

                        salesManagerArrayList.add(newSales);
                    }
                } else {

                    SalesManager newSales = new SalesManager(jsonObject.getString("category"), jsonObject.getInt("menuid"));

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

                Order order = new Order(jsonObject.getString("category"), jsonObject.getInt("menuid"), jsonObject.getInt("id"), jsonObject.getString("name") + "adsfasdf", jsonObject.getInt("qty"), jsonObject.getInt("price"));
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
