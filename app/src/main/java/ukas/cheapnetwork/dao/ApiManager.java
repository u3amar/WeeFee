package ukas.cheapnetwork.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.models.ConnectionInfo;
import ukas.cheapnetwork.models.NetworkMonitor;
import ukas.cheapnetwork.models.SSIDMapping;
import ukas.cheapnetwork.models.TransmitInfo;

/**
 * Created by usama on 4/30/16.
 */
public class ApiManager {
    private static final String ENDPOINT_BASE = "http://52.38.69.109/",
            ENDPOINT_CREATE_SSID_MAPPING = ENDPOINT_BASE + "create_SSID_mapping.php",
            ENDPOINT_UPLOAD_DATA_TRANSFER = ENDPOINT_BASE + "data_transfers.php",
            ENDPOINT_GET_CONNECTION_INFO = ENDPOINT_BASE + "get_connection_info.php";

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
                .add("ssid", mapping.getRawSSID())
                .add("mac_addr", mapping.getMacAddress())
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT_CREATE_SSID_MAPPING)
                .post(requestBody)
                .build();

        new VoidRequestConsumer().enqueueCall(request, mClient, callback);
    }

    public void saveDataTransfer(NetworkMonitor monitor, final NetworkCallback<Void> callback) {
        TransmitInfo transmitInfo = monitor.getReceivedTransmitInfo();
        SSIDMapping ssidMapping = transmitInfo.getSSIDMapping();

        RequestBody requestBody = new FormBody.Builder()
                .add("ssid", ssidMapping.getRawSSID())
                .add("mac_addr", ssidMapping.getMacAddress())
                .add("uploaded", Long.toString(monitor.getTransferredTransmitInfo().getBytes()))
                .add("downloaded", Long.toString(monitor.getReceivedTransmitInfo().getBytes()))
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT_UPLOAD_DATA_TRANSFER)
                .post(requestBody)
                .build();

        new VoidRequestConsumer().enqueueCall(request, mClient, callback);
    }

    public void getConnectionInfo(String ssid, final NetworkCallback<ConnectionInfo> callback) {
        ssid = ssid.replaceAll("\"", "");

        Request request = new Request.Builder()
                .url(ENDPOINT_GET_CONNECTION_INFO + "?ssid=" + ssid)
                .build();

        new RequestConsumer<ConnectionInfo>() {
            @Override
            public ConnectionInfo parseResponse(Response response) {
                try {
                    String responseString = response.body().string();
                    Gson gson = new GsonBuilder().create();
                    return gson.fromJson(responseString, ConnectionInfo.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.enqueueCall(request, mClient, callback);
    }

    private class VoidRequestConsumer extends RequestConsumer<Void> {
        @Override
        public Void parseResponse(Response response) {
            return null;
        }
    }
}
