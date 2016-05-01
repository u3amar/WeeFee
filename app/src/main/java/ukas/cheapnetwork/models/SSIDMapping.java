package ukas.cheapnetwork.models;

/**
 * Created by usama on 4/30/16.
 */
public class SSIDMapping {
    private String mSSID, mMacAddress;

    public SSIDMapping(String mSSID, String mMacAddress) {
        this.mSSID = mSSID;
        this.mMacAddress = mMacAddress;
    }

    public String getSSID() {
        return mSSID;
    }

    public String getRawSSID() {
        return mSSID.replaceAll("\"", "");
    }

    public void setSSID(String mSSID) {
        this.mSSID = mSSID;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }
}
