package ukas.cheapnetwork.dao;

import android.os.Handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.models.NetworkMonitor;
import ukas.cheapnetwork.models.SSIDMapping;
import ukas.cheapnetwork.models.TransmitInfo;

/**
 * Created by usama on 4/30/16.
 */
public class ApiManager {
    private static final String ENDPOINT_BASE = "http://52.38.69.109/",
            ENDPOINT_CREATE_SSID_MAPPING = ENDPOINT_BASE + "create_SSID_mapping.php",
            ENDPOINT_UPLOAD_DATA_TRANSFER = ENDPOINT_BASE + "data_transfers.php";

    private static ApiManager mManager;
    private OkHttpClient mClient;

    private ApiManager() {
        mClient = new OkHttpClient();
    }

    public synchronized static ApiManager getInstance() {
        if (mManager == null) {
            mManager = new ApiManager();
        }

        return mManager;
    }

    public void saveSSIDMapping(SSIDMapping mapping, final NetworkCallback<Void> callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("ssid", mapping.getSSID())
                .add("mac_addr", mapping.getMacAddress())
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT_CREATE_SSID_MAPPING)
                .post(requestBody)
                .build();

        enqueueCall(request, callback);
    }

    public void saveDataTransfer(NetworkMonitor monitor, final NetworkCallback<Void> callback) {
        TransmitInfo transmitInfo = monitor.getReceivedTransmitInfo();
        SSIDMapping ssidMapping = transmitInfo.getSSIDMapping();

        RequestBody requestBody = new FormBody.Builder()
                .add("ssid", ssidMapping.getSSID())
                .add("mac_addr", ssidMapping.getMacAddress())
                .add("uploaded", Long.toString(monitor.getTransferredTransmitInfo().getBytes()))
                .add("downloaded", Long.toString(monitor.getReceivedTransmitInfo().getBytes()))
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT_UPLOAD_DATA_TRANSFER)
                .post(requestBody)
                .build();

        enqueueCall(request, callback);
    }

    private void enqueueCall(Request request, final NetworkCallback<Void> callback) {
        Handler handler = new Handler();
        mClient.newCall(request)
                .enqueue(new MainThreadCallback<>(callback, handler));
    }

    public class MainThreadCallback<T extends NetworkCallback> implements Callback {
        private Handler mHandler;
        private T mCallback;

        public MainThreadCallback(T callback, Handler handler) {
            mHandler = handler;
            mCallback = callback;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(HttpURLConnection.HTTP_BAD_REQUEST, new Exception(e.getMessage()));
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        mCallback.onSuccess(null);
                    } else {
                        mCallback.onError(response.code(), new ApiException("Error saving transmit info"));
                    }
                }
            });
        }
    }

    public class ApiException extends Exception {
        public ApiException() {
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }

        public ApiException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ApiException(Throwable throwable) {
            super(throwable);
        }
    }
}
