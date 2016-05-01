package ukas.cheapnetwork.activities;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ukas.cheapnetwork.R;
import ukas.cheapnetwork.utils.NetworkUtils;

public class DataGraphActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_graph);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_graph_disconnect:
                exitNetwork();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exitNetwork() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (NetworkUtils.isWiFiHotspotOn(this)) {
            NetworkUtils.setWifiApEnabled(NetworkUtils.getWifiApConfiguration(wifiManager), false, wifiManager);
        } else if (NetworkUtils.isWifiConnected(this)){
            wifiManager.disconnect();
        }

        finish();
    }
}
