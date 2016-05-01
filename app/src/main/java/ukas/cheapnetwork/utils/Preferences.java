package ukas.cheapnetwork.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by usama on 4/30/16.
 */
public class Preferences {
    private final static String KEY_MAC_ADDRESS = "mac_address_key",
            SAVED_SSID_KEY = "saved_ssid_key";

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
                .putString(SAVED_SSID_KEY, ssid)
                .commit();
    }

    @Nullable
    public static String getSavedSSID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SAVED_SSID_KEY, null);
    }
}
