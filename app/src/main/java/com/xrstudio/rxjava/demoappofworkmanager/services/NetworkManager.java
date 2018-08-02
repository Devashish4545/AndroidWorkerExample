package com.xrstudio.rxjava.demoappofworkmanager.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xrstudio.rxjava.demoappofworkmanager.App;

/**
 * Created by PREMIUM on 20-07-2018.
 */

public class NetworkManager {

    private final ConnectivityManager connectivityManager;

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }

        return instance;
    }

    private NetworkManager() {
        App application = App.getInstance();

        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public void onNetworkChange() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED && App.getInstance().getConnection() != null && !App.getInstance().getConnection().isAuthenticated()) {
            onAvailable();
        }
    }

    private void onAvailable() {
        ConnectionManager.getInstance().login();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
