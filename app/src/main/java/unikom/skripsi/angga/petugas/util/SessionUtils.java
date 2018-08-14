package unikom.skripsi.angga.petugas.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import unikom.skripsi.angga.petugas.helper.Config;
import unikom.skripsi.angga.petugas.model.UserModel;

public class SessionUtils {
    public static boolean login(Context context, UserModel userModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Config.KEY_USER_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String userJson = new Gson().toJson(userModel);
        editor.putString(Config.USER_SESSION, userJson);
        editor.apply();
        return true;
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Config.KEY_USER_SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(Config.USER_SESSION, null);
        if (userJson != null) {
            return true;
        } else {
            return false;
        }
    }

    public static UserModel getLoggedUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Config.KEY_USER_SESSION, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(Config.USER_SESSION, null);
        if (userJson != null) {
            UserModel user = new Gson().fromJson(userJson, UserModel.class);
            return user;
        } else
            return null;
    }

    public static boolean logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Config.KEY_USER_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        return true;
    }
}
