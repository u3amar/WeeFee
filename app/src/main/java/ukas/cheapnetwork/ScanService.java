package ukas.cheapnetwork;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

/**
 * Created by usama on 4/29/16.
 */
public class ScanService extends IntentService {
    private WifiManager mWifiManager;

    public ScanService(String name) {
        super(name);
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    public static void start(Context context) {
        context.startService(new Intent(context, ScanService.class));
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mWifiManager.startScan();
    }

    public void getNearbySSIDS() {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.getConfiguredNetworks();

    }
}
