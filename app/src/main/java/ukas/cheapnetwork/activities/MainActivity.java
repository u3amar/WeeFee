package ukas.cheapnetwork.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ukas.cheapnetwork.R;
import ukas.cheapnetwork.services.ScanService;
import ukas.cheapnetwork.utils.Utils;

public class MainActivity extends AppCompatActivity {
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
}
