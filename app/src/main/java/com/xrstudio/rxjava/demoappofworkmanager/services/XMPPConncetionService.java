package com.xrstudio.rxjava.demoappofworkmanager.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.xrstudio.rxjava.demoappofworkmanager.App;

import org.jivesoftware.smack.packet.Presence;

public class XMPPConncetionService extends Service {
    private static final XMPPConncetionService ourInstance = new XMPPConncetionService();

    public static XMPPConncetionService getInstance() {
        return ourInstance;
    }

    public XMPPConncetionService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (NetworkManager.isNetworkAvailable()) {
            ConnectionManager.getInstance().login();
        }
        //service will immediate destroy after service killed.
        // If we start sticky then application keeping crashing.
        return START_NOT_STICKY;
    }

    public void onBecameForeground() {


        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {

            public void run() {
                try {
                    Presence p = new Presence(Presence.Type.available, "Online", 0, Presence.Mode.available);
                    App.getInstance().getConnection().sendStanza(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBecameBackground() {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {

            public void run() {

                try {
                    Presence p = new Presence(Presence.Type.available, "Offline", 0, Presence.Mode.dnd);
                    //p.setMode(Presence.Mode.away);
                    App.getInstance().getConnection().sendStanza(p);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onBecameBackground();
    }
}
