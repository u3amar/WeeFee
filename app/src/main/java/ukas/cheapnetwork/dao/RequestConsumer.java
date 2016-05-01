package ukas.cheapnetwork.dao;

import android.os.Handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ukas.cheapnetwork.interfaces.NetworkCallback;
import ukas.cheapnetwork.utils.ApiException;

/**
 * Created by usama on 4/30/16.
 */
public abstract class RequestConsumer<T> {
    public void enqueueCall(Request request, OkHttpClient client, final NetworkCallback<T> callback) {
        Handler handler = new Handler();
        client.newCall(request)
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
                        mCallback.onSuccess(parseResponse(response));
                    } else {
                        mCallback.onError(response.code(), new ApiException("Error saving transmit info"));
                    }
                }
            });
        }
    }

    public abstract T parseResponse(Response response);
}
