package com.mobile.mobileordering.util;

import java.util.ArrayList;
import java.util.List;

public class Cache {

    private static volatile List<PendingItem> list = new ArrayList<>();

    public static void clear(){
        list.clear();
    }

    public static void put(PendingItem item){
        list.add(item);
    }

    public static List<PendingItem> get(){
        return list;
    }

}
