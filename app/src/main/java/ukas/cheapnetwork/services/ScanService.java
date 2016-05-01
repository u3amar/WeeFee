package ukas.cheapnetwork.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import ukas.cheapnetwork.R;
import ukas.cheapnetwork.utils.NetworkUtils;

/**
 * Created by usama on 4/29/16.
 */
public class ScanService extends IntentService {
    public static String TAG = "ScanService";

    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;

    public ScanService() {
        super("ScanService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static void start(Context context) {
        context.startService(new Intent(context, ScanService.class));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!NetworkUtils.isWifiConnected(mConnectivityManager)) {
            startScan();
        } else {
            String ssid = mWifiManager.getConnectionInfo().getSSID();
            if (isDataShareHotspot(ssid)) {
                onWiFiConnected();
            } else {
                mWifiManager.disconnect();
                startScan();
            }
        }
    }

    private void startScan() {
        mWifiManager.startScan();
        try {
            connectToNearbyNetwork();
            onWiFiConnected();
        } catch (AccessPointConnectionException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void onWiFiConnected() {
        DataUsageService.startService(this);
    }

    public void connectToNearbyNetwork() throws AccessPointConnectionException {
        mWifiManager.setWifiEnabled(true);

        List<ScanResult> scanResults = mWifiManager.getScanResults();
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                if (isDataShareHotspot(scanResult.SSID) && isOpenNetwork(scanResult)) {
                    connectToNetwork(scanResult.SSID);
                }
            }
        }
    }

    public void connectToNetwork(String SSID) throws AccessPointConnectionException {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = String.format("\"%s\"", SSID);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int netId = mWifiManager.addNetwork(wifiConfiguration);
        if (mWifiManager.enableNetwork(netId, true)) {
            mWifiManager.reconnect();
            Log.d(TAG, "Successfully connected to network");
        } else {
            Log.e(TAG, "Error connecting to network");
            throw new AccessPointConnectionException("Error connecting to WiFi network: " + SSID);
        }
    }

    public boolean isOpenNetwork(ScanResult scanResult) {
        String capabilities = scanResult.capabilities;
        return !capabilities.toUpperCase().contains("WEP") && !capabilities.toUpperCase().contains("WPA");
    }

    public boolean isDataShareHotspot(String SSID) {
        return SSID.contains(getString(R.string.app_name));
    }

    public class AccessPointConnectionException extends Exception {
        public AccessPointConnectionException() {
        }

        public AccessPointConnectionException(String detailMessage) {
            super(detailMessage);
        }

        public AccessPointConnectionException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public AccessPointConnectionException(Throwable throwable) {
            super(throwable);
        }
    }
}
