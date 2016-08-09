package com.tomleto.roix.testtaskgallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by bsr on 07.08.2016.
 */
public class DataManager {
    //this value stores active image item id
    public static int currItemId=0;

    //strings constanst, its need to saving in prefs
    public static final String PREFS_NAME = "testtaskgallery.preferences.images";
    public static final String COMMENT_PREFIX = "comment_";
    public static final String FAVORITES_PREFIX = "favor_";
    public static final String ANIMATION_KEY="animation";
    public static final String ANIMATION_TIME_KEY="animation_time";
    public static final String FAVORITES_KEY="favorites";
    public static final String RANDOM_KEY="random";
    public static final String ANIMATION_CHANGE="anim_change";


    //final forming array our items from json on assets
    public static ArrayList<ImageItem> loadImageItems(Context context){
        ArrayList<ImageItem> ret=new ArrayList<>();
        String json=loadJsonStringFromAssets(context);
        try {
            JSONObject object=new JSONObject(json);
            JSONArray array=object.getJSONArray("array");
            for(int i=0;i<array.length();i++){
                JSONObject o=array.getJSONObject(i);
                JSONObject im=o.getJSONObject("image");
                int id=im.getInt("id");
                int count=im.getInt("count");
                String url=im.getString("url");
                if(currItemId==id)
                Log.d("aaaa",id+" loadImageItems json");

                String comment=getStringFromPrefs(context,COMMENT_PREFIX+id,"");
                boolean isFavorites=getBooleanFromPrefs(context,FAVORITES_PREFIX+id,false);
                ImageItem item=new ImageItem(id,count,url,comment,isFavorites);
                ret.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return ret;
    }

    private static String loadJsonStringFromAssets(Context context){
        String text = "image_json.dat";
        byte[] buffer = null;
        InputStream is;
        try {
            is = context.getAssets().open(text);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str_data = new String(buffer);
        return str_data;
    }

    //organize manage shared prefs
    public static String getStringFromPrefs(Context context,String key, String def){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(key,def);
    }
    public static void setStringInPrefs(Context context,String key, String val){
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(key, val).commit();
    }

    public static int getIntFromPrefs(Context context,String key, int def){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(key,def);
    }
    public static void setIntInPrefs(Context context,String key, int val){
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(key, val).commit();
    }

    public static boolean getBooleanFromPrefs(Context context,String key, boolean def){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(key,def);
    }
    public static void setBooleanInPrefs(Context context,String key, boolean val){
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, val).commit();
    }

}
