package ukas.cheapnetwork.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import ukas.cheapnetwork.R;

/**
 * Created by usama on 4/30/16.
 */
public class BaseActivity extends AppCompatActivity {
    private Dialog mDialog;

    @Override
    protected void onPause() {
        super.onPause();
        cancelDialog();
    }

    public void showDialog(Dialog d) {
        if (mDialog != null) {
            mDialog.cancel();
        }

        mDialog = d;
        mDialog.show();
    }

    public void showDefaultProgressDialog() {
        showDialog(ProgressDialog.show(this, getString(R.string.title_progress_loading), getString(R.string.title_progress_please_wait)));
    }

    public void cancelDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
    }
}
