package com.xrstudio.rxjava.demoappofworkmanager.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by PREMIUM on 20-07-2018.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkManager.getInstance().onNetworkChange();
    }
}
