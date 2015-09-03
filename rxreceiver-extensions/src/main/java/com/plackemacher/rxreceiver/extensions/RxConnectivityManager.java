package com.plackemacher.rxreceiver.extensions;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.plackemacher.rxreceiver.RxReceiver;
import com.plackemacher.rxreceiver.extensions.data.ConnectivityActionData;

import rx.Observable;
import rx.functions.Func1;

public class RxConnectivityManager {

    private static final IntentFilter INTENT_FILTER = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    public Observable<ConnectivityActionData> forContext(final Context context) {
        return RxReceiver.fromBroadcast(context, INTENT_FILTER)
                .map(new Func1<Intent, ConnectivityActionData>() {
                    @Override
                    public ConnectivityActionData call(Intent intent) {
                        NetworkInfo activeNetworkInfo = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        }

                        return new ConnectivityActionData(intent.getExtras(), activeNetworkInfo);
                    }
                });
    }
}
