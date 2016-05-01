package ukas.cheapnetwork.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ukas.cheapnetwork.R;
import ukas.cheapnetwork.services.ScanService;
import ukas.cheapnetwork.utils.NetworkUtils;
import ukas.cheapnetwork.utils.Utils;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (!Utils.checkSystemWritePermission(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        if ((NetworkUtils.isWiFiHotspotOn(this) || NetworkUtils.isWifiConnected(this))
                && NetworkUtils.isConnectedToWeeFeeNetwork(this)) {
            onConnected();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNetworkChangeReceiver();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.activity_main_become_host_button)
    public void onHostButtonClicked() {
        Intent intent = new Intent(this, SSIDPromptActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.activity_main_become_receiver_button)
    public void onReceiverButtonClicked() {
        ScanService.start(this);
    }

    public void onConnected() {
        Intent dataGraphActivity = new Intent(this, DataGraphActivity.class);
        startActivity(dataGraphActivity);
        finish();
    }

    public void initNetworkChangeReceiver() {
        registerReceiver(networkStateChangeReceiver, NetworkUtils.getNetworkStateChangeFilter());
    }

    private BroadcastReceiver networkStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isWifiConnected(context)) {
                onConnected();
            }
        }
    };
}
