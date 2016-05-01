package ukas.cheapnetwork.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ukas.cheapnetwork.R;
import ukas.cheapnetwork.dao.ApiManager;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.models.SSIDMapping;
import ukas.cheapnetwork.utils.NetworkUtils;
import ukas.cheapnetwork.utils.Preferences;
import ukas.cheapnetwork.utils.Utils;

public class SSIDPromptActivity extends BaseActivity {
    @BindView(R.id.activity_type_in_SSID_textview) TextView mSSIDPromptTextView;
    @BindView(R.id.activity_type_network_settings_button) Button mNetworkSettingsTransitionButton;

    private final int SSID_LENGTH = 6;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_in_ssid);
        ButterKnife.bind(this);

        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initHotspot();
    }

    private void initHotspot() {
        String macAddress = NetworkUtils.getMacAddress(mWifiManager);
        if (macAddress != null) {
            Preferences.saveMacAddress(macAddress, this);
        }

        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = Preferences.getSavedSSID(this);
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        showDefaultProgressDialog();
        if (!NetworkUtils.isWiFiHotspotOn(this)) {
            if (!NetworkUtils.setWifiApEnabled(configuration, true, mWifiManager)) {
                cancelDialog();
            }
        } else if (!NetworkUtils.setWifiApConfiguration(configuration, mWifiManager)) {
            cancelDialog();
        }
    }

    private String initSSID() {
        String savedSSID = Preferences.getSavedSSID(this);
        if (savedSSID == null) {
            savedSSID = generateSSID();
            Preferences.saveSSID(savedSSID, this);
        }

        return savedSSID;
    }

    @Override
    public void onResume() {
        super.onResume();
        beginHotspotPolling();
    }

    @OnClick(R.id.activity_type_network_settings_button)
    public void onNetworkButtonClicked() {
        final Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void beginHotspotPolling() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mNetworkSettingsTransitionButton.setEnabled(true);
                mSSIDPromptTextView.setText("Please turn on hotspot with SSID: " + initSSID());

                if (!NetworkUtils.isWiFiHotspotOn(SSIDPromptActivity.this)) {
                    handler.postDelayed(this, 1000);
                } else {
                    if (!isHotspotNameValid()) {
                        mSSIDPromptTextView.setText("Please change your SSID to " +
                                Preferences.getSavedSSID(SSIDPromptActivity.this) + " in order to receive credits!");
                    } else {
                        onHotspotEnabled();
                    }
                }
            }
        });
    }

    private boolean isHotspotNameValid() {
        String currSSID = NetworkUtils.getWifiApConfiguration(mWifiManager).SSID;
        return currSSID.equals(Preferences.getSavedSSID(this));
    }

    private void onHotspotEnabled() {
        showDefaultProgressDialog();

        ApiManager.getInstance()
                .saveSSIDMapping(new SSIDMapping(Preferences.getSavedSSID(this), Preferences.getSavedMacAddress(this)),
                        new NetworkCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                cancelDialog();
                                onSSIDMappingSaved();
                            }

                            @Override
                            public void onError(int statusCode, Exception error) {
                                cancelDialog();
                            }
                        });
    }

    private void onSSIDMappingSaved() {
        mNetworkSettingsTransitionButton.setText(R.string.activity_type_in_SSID_ready_to_go);
        mNetworkSettingsTransitionButton.setEnabled(false);
        mSSIDPromptTextView.setText(R.string.activity_type_in_SSID_hotspot);
    }

    private String generateSSID() {
        return getString(R.string.app_name) + Utils.generateRandomString(SSID_LENGTH);
    }
}
