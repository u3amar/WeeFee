package ukas.cheapnetwork.models;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;

import ukas.cheapnetwork.utils.NetworkUtils;

/**
 * Created by usama on 4/30/16.
 */
public class NetworkMonitor {
    private SSIDMapping mSSID;
    private long mStartReceivedBytes, mStartTransferredBytes, mReceivedBytes, mTransferredBytes;

    public NetworkMonitor(Context context) {
        this(new SSIDMapping(NetworkUtils.getSSID(context), ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .getConnectionInfo().getMacAddress()));
    }

    public NetworkMonitor(SSIDMapping SSID) {
        mSSID = SSID;
        reset();
    }

    public void updateTransferBytes() {
        if (mStartReceivedBytes == -1) {
            mStartReceivedBytes = TrafficStats.getTotalRxBytes();
        }

        if (mStartTransferredBytes == -1) {
            mStartTransferredBytes = TrafficStats.getTotalTxBytes();
        }

        mReceivedBytes = TrafficStats.getTotalRxBytes() - mStartReceivedBytes;
        mTransferredBytes = TrafficStats.getTotalTxBytes() - mStartTransferredBytes;
    }

    public void reset() {
        mStartReceivedBytes = -1;
        mStartTransferredBytes = -1;
        mReceivedBytes = 0;
        mTransferredBytes = 0;
    }

    public TransmitInfo getTransferredTransmitInfo() {
        return new TransmitInfo(mSSID, mTransferredBytes, true);
    }

    public TransmitInfo getReceivedTransmitInfo() {
        return new TransmitInfo(mSSID, mReceivedBytes, false);
    }
}
