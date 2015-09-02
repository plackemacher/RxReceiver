package com.plackemacher.rxreceiver.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

import com.plackemacher.rxreceiver.RxReceiver;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private TextView mTextView;

    private ConnectivityManager mConnectivityManager;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(android.R.id.text1);

        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        mSubscription = RxReceiver.fromBroadcast(this, intentFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Intent>() {
                    @Override
                    public void call(Intent intent) {
                        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
                        mTextView.setText(Boolean.toString(noConnectivity));
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSubscription.unsubscribe();
    }
}
