package uk.ac.tees.w9501293.travethon.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public ProfileManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("profile",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    private void addUsername(String username){
        editor.putString("username",username);
        editor.apply();
    }

    private void addPhotoUrl(String photoUrl){
        editor.putString("photoUrl",photoUrl);
        editor.apply();
    }

    private void addEmail(String photoUrl){
        editor.putString("photoUrl",photoUrl);
        editor.apply();
    }

    private String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    private String getString(String key,String defaultValue){
        return sharedPreferences.getString(key,defaultValue);
    }

    private void putBoolean(String key, Boolean value){
        editor.putBoolean(key,value);
        editor.apply();
    }

    private Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    private Boolean getBoolean(String key,Boolean defaultValue){
        return sharedPreferences.getBoolean(key,defaultValue);
    }
}
