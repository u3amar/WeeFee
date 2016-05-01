package ukas.cheapnetwork.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by usama on 4/30/16.
 */
public class ConnectionInfo {
    @SerializedName("values")
    private long[] bytesSentPerUser;

    @SerializedName("total")
    private long totalBytesSent;

    public long[] getBytesSentPerUser() {
        return bytesSentPerUser;
    }

    public void setBytesSentPerUser(long[] bytesSentPerUser) {
        this.bytesSentPerUser = bytesSentPerUser;
    }

    public long getTotalBytesSent() {
        return totalBytesSent;
    }

    public void setTotalBytesSent(long totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
    }
}
