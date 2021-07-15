package com.ommi.uisrael.odoomobilemoduleintegration.datos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedData {

    private static  final String MyPREFERENCES = "MyPrefs";
    private  static SharedPreferences sharedPreferences;

    public  static  String getKey(Context c, String key){

        sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return  sharedPreferences.getString(key, "");


    }

    public static void  setKey(Context c, String key , String value){

        sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
