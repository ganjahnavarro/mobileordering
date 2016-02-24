package com.mobile.mobileordering.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by simplyph on 1/11/2016.
 */
public class LayoutManager {

    private Context context;

    public LayoutManager(Context c){
        context = c;
    }

    public View inflate(int resourceID){
        View view;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return layoutInflater.inflate(resourceID, null);
    }

    public View inflate(int resourceID, ViewGroup parent){
        View view;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return layoutInflater.inflate(resourceID, parent, false);
    }
}
