package ukas.cheapnetwork.utils;

/**
 * Created by usama on 4/30/16.
 */
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
