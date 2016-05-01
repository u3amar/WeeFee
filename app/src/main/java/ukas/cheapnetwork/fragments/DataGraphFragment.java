package ukas.cheapnetwork.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ukas.cheapnetwork.R;
import ukas.cheapnetwork.dao.ApiManager;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.models.ConnectionInfo;
import ukas.cheapnetwork.services.DataUsageService;
import ukas.cheapnetwork.utils.NetworkUtils;
import ukas.cheapnetwork.views.GraphView;

/**
 * Created by usama on 4/30/16.
 */
public class DataGraphFragment extends Fragment {
    @BindView(R.id.data_graph_view) GraphView mGraphView;
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_graph, container, false);
        ButterKnife.bind(this, rootView);
        beginDataNodeQuery();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void beginDataNodeQuery() {
        mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                loadDataNodes();
                mHandler.postDelayed(this, DataUsageService.UPLOAD_INTERVAL_MILLIS);
            }
        });
    }

    public void loadDataNodes() {
        String ssid = NetworkUtils.getSSID(getActivity());
        ApiManager.getInstance()
                .getConnectionInfo(ssid, new NetworkCallback<ConnectionInfo>() {
                    @Override
                    public void onSuccess(ConnectionInfo data) {
                        updateGraphView(data);
                    }

                    @Override
                    public void onError(int statusCode, Exception error) {
                    }
                });
    }

    private void updateGraphView(ConnectionInfo data) {
        mGraphView.clearNodes();

        long[] bytesPerUser = data.getBytesSentPerUser();
        if (bytesPerUser != null) {
            for (int i = 0; i < bytesPerUser.length; i++) {
                mGraphView.addNode(bytesPerUser[i]);
            }

            mGraphView.invalidate();
        }
    }
}
