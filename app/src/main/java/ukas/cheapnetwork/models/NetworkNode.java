package ukas.cheapnetwork.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usama on 4/30/16.
 */
public class NetworkNode<T> {
    private NetworkNode<T> mParent;
    private List<NetworkNode<T>> mChildren = new ArrayList<>();
    private T mData;

    public NetworkNode(NetworkNode<T> mParent) {
        this.mParent = mParent;
    }

    public void addChild(NetworkNode<T> child) {
        mChildren.add(child);
    }

    public List<NetworkNode<T>> getChildren() {
        return mChildren;
    }

    public void setData(T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }
}
