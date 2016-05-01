package ukas.cheapnetwork.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ukas.cheapnetwork.utils.NetworkUtils;
import ukas.cheapnetwork.utils.Preferences;

/**
 * Created by usama on 4/29/16.
 */
public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtils.isWifiConnected(context) && Preferences.isConnected(context)) {
            ScanService.start(context);
        }
    }
}
