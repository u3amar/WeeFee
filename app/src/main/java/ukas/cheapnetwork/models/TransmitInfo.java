package ukas.cheapnetwork.models;

/**
 * Created by usama on 4/30/16.
 */
public class TransmitInfo {
    private SSIDMapping mSSIDMapping;
    private long mBytes;
    private boolean mIsUpload;

    public TransmitInfo() {
    }

    public TransmitInfo(SSIDMapping mSSID, long mBytes, boolean mIsUpload) {
        this.mSSIDMapping = mSSID;
        this.mBytes = mBytes;
        this.mIsUpload = mIsUpload;
    }

    public long getBytes() {
        return mBytes;
    }

    public void setBytes(long mBytes) {
        this.mBytes = mBytes;
    }

    public boolean isUpload() {
        return mIsUpload;
    }

    public void setIsUpload(boolean mIsUpload) {
        this.mIsUpload = mIsUpload;
    }

    public SSIDMapping getSSIDMapping() {
        return mSSIDMapping;
    }

    public void setSSIDMapping(SSIDMapping mSSIDMapping) {
        this.mSSIDMapping = mSSIDMapping;
    }
}
