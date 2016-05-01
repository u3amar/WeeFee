package ukas.cheapnetwork.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by usama on 4/30/16.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static String generateRandomString(int length) {
        final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String randomString = "";
        for (int i = 0; i < length; i++) {
            char randomLetter = alphabet.charAt((int) (Math.random() * (alphabet.length() - 1)));
            randomString += randomLetter;
        }

        return randomString;
    }

    public static boolean checkSystemWritePermission(Context context) {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(context);
        }

        return retVal;
    }
}
