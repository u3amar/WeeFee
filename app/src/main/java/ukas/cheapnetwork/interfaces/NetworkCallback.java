package ukas.cheapnetwork.interfaces;

/**
 * Created by usama on 4/30/16.
 */
public interface NetworkCallback<T> {
    void onSuccess(T data);

    void onError(int statusCode, Exception error);
}
