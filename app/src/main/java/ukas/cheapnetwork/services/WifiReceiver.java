package ukas.cheapnetwork.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ukas.cheapnetwork.services.ScanService;
import ukas.cheapnetwork.utils.NetworkUtils;

/**
 * Created by usama on 4/29/16.
 */
public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtils.isWifiConnected(context)) {
            ScanService.start(context);
        }
    }
}
