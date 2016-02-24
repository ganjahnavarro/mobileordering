package com.mobile.mobileordering.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by simplyph on 1/11/2016.
 */
public class JSONManager {

    private Context context;
    private String JSONFilePath;

    public JSONManager(Context c, String path) {
        context = c;
        JSONFilePath = path;
    }

    public JSONArray getData() {
        JSONArray data = null;
        InputStream fs = null;
        StringBuffer b = new StringBuffer();
        AssetManager am = context.getAssets();

        try {
            fs = am.open(JSONFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedInputStream bis = new BufferedInputStream(fs);

        try {
            while (bis.available() != 0) {
                char c = (char) bis.read();
                b.append(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            data = new JSONArray(b.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
