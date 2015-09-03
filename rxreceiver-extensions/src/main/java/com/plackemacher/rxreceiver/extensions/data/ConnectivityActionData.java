package com.plackemacher.rxreceiver.extensions.data;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class ConnectivityActionData {
    private final String extraInfo;
    private final boolean failover;
    private final Network network;
    private final NetworkInfo networkInfo;
    private final NetworkRequest networkRequest;
    private final int networkType;
    private final boolean noConnectivity;
    private final NetworkInfo otherNetworkInfo;
    private final String reason;

    public ConnectivityActionData(@NonNull Bundle extras, NetworkInfo activeNetworkInfo) {
        extraInfo = extras.getString(ConnectivityManager.EXTRA_EXTRA_INFO);
        failover = extras.getBoolean(ConnectivityManager.EXTRA_IS_FAILOVER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            network = extras.getParcelable(ConnectivityManager.EXTRA_NETWORK);
            networkRequest = extras.getParcelable(ConnectivityManager.EXTRA_NETWORK_REQUEST);
        } else {
            network = null;
            networkRequest = null;
        }

        networkInfo = extras.getParcelable(ConnectivityManager.EXTRA_NETWORK_INFO);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            networkType = extras.getInt(ConnectivityManager.EXTRA_NETWORK_TYPE);
        } else {
            networkType = -1;
        }

        noConnectivity = extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        otherNetworkInfo = extras.getParcelable(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
        reason = extras.getString(ConnectivityManager.EXTRA_REASON);
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public boolean isFailover() {
        return failover;
    }

    public Network getNetwork() {
        return network;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public NetworkRequest getNetworkRequest() {
        return networkRequest;
    }

    public int getNetworkType() {
        return networkType;
    }

    public boolean hasNoConnectivity() {
        return noConnectivity;
    }

    public NetworkInfo getOtherNetworkInfo() {
        return otherNetworkInfo;
    }

    public String getReason() {
        return reason;
    }
}
