package ukas.cheapnetwork.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ukas.cheapnetwork.dao.ApiManager;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.models.NetworkMonitor;
import ukas.cheapnetwork.utils.NetworkUtils;

/**
 * Created by usama on 4/30/16.
 */
public class DataUsageService extends Service {
    public static final String TAG = "DataUsageService";
    private final long UPLOAD_INTERVAL_MILLIS = 10 * 1000;

    private NetworkMonitor mNetworkMonitor;
    private long mLastUploadTime = System.currentTimeMillis();

    public static void startService(Context context) {
        context.startService(new Intent(context, DataUsageService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNetworkChangeReceiver();

        mNetworkMonitor = new NetworkMonitor((WifiManager) getSystemService(WIFI_SERVICE));
        beginNetworkMonitoring();
    }

    public void beginNetworkMonitoring() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateNetworkMonitor();
                handler.postDelayed(this, 100);
            }
        });
    }

    public void updateNetworkMonitor() {
        mNetworkMonitor.updateTransferBytes();

        long dt = System.currentTimeMillis() - mLastUploadTime;
        if (dt > UPLOAD_INTERVAL_MILLIS) {
            syncDataTransfer();
            mLastUploadTime = System.currentTimeMillis();
        }

        Log.d(TAG, "Received " + mNetworkMonitor.getReceivedTransmitInfo().getBytes() + " bytes");
    }

    public void syncDataTransfer() {
        ApiManager.getInstance()
                .saveDataTransfer(mNetworkMonitor, new NetworkCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "Successfully synced data");
                        mNetworkMonitor.reset();
                    }

                    @Override
                    public void onError(int statusCode, Exception error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
    }

    public void initNetworkChangeReceiver() {
        registerReceiver(networkStateChangeReceiver, NetworkUtils.getNetworkStateChangeFilter());
    }

    private BroadcastReceiver networkStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isWifiConnected(context)) {
                stopSelf();
            }
        }
    };
}
