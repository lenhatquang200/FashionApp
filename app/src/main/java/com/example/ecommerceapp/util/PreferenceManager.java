package com.example.ecommerceapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for managing SharedPreferences
 */
public class PreferenceManager {
    private static final String PREF_NAME = "EcommerceAppPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    
    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    /**
     * Save user session information
     * @param userId The user ID
     * @param username The username
     */
    public void saveUserSession(long userId, String username) {
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    /**
     * Get logged in user ID
     * @return user ID or -1 if not logged in
     */
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }
    
    /**
     * Get logged in username
     * @return username or empty string if not logged in
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
    
    /**
     * Clear user session (logout)
     */
    public void clearUserSession() {
        editor.clear();
        editor.apply();
    }
}
