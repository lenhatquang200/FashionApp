package com.ecommerce.app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    private static final String PREFS_NAME = "ecommerce_prefs";
    private static final String KEY_DATABASE_INITIALIZED = "database_initialized";
    private static final String KEY_USER_ID = "user_id";
    
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static boolean isDatabaseInitialized(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_DATABASE_INITIALIZED, false);
    }
    
    public static void setDatabaseInitialized(Context context, boolean initialized) {
        getSharedPreferences(context).edit().putBoolean(KEY_DATABASE_INITIALIZED, initialized).apply();
    }
    
    public static long getUserId(Context context) {
        return getSharedPreferences(context).getLong(KEY_USER_ID, 1); // Default user ID is 1
    }
    
    public static void setUserId(Context context, long userId) {
        getSharedPreferences(context).edit().putLong(KEY_USER_ID, userId).apply();
    }
}
