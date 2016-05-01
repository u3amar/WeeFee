package ukas.cheapnetwork.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by usama on 4/30/16.
 */
public class Preferences {
    private final static String KEY_MAC_ADDRESS = "mac_address_key",
            KEY_SAVED_SSID = "saved_ssid_key",
            KEY_IS_CONNECTED = "is_connected_key";

    public static String getSavedMacAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_MAC_ADDRESS, null);
    }

    public static void saveMacAddress(String address, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_MAC_ADDRESS, address)
                .commit();
    }

    public static void saveSSID(String ssid, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_SAVED_SSID, ssid)
                .commit();
    }

    @Nullable
    public static String getSavedSSID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_SAVED_SSID, null);
    }

    public static boolean isConnected(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_IS_CONNECTED, false);
    }

    public static void setIsConnected(boolean isConnected, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_IS_CONNECTED, isConnected)
                .commit();
    }
}
